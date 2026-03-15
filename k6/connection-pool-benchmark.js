/**
 * 커넥션 풀 병목 분석 벤치마크 — Tomcat · HikariCP · Redis
 *
 * ───────────────────────────────────────────────────────────────
 * 목적
 *   Tomcat 스레드 풀 / HikariCP DB 커넥션 풀 / Redis(Lettuce) 중
 *   어느 레이어가 먼저 포화되는지 확인하고 병목 지점을 특정한다.
 *
 * 시나리오 구성
 *   1. cache_readers   — Redis 캐시 HIT 위주 읽기 (Redis 커넥션 부하)
 *   2. db_readers      — 캐시 없는 순수 DB 읽기   (HikariCP 부하)
 *   3. cache_busters   — 쓰기 후 캐시 evict → 즉시 읽기 (캐시 MISS 연쇄)
 *   4. db_writers      — 강의실/교시 생성·삭제 반복 (HikariCP 쓰기 트랜잭션)
 *
 * 판독 기준
 *   - cache_readers 지연 ↑, db_readers 지연 낮음
 *       → Redis 연결 또는 Lettuce 처리량 병목
 *   - db_readers 지연 ↑, cache_readers 지연 낮음
 *       → HikariCP 커넥션 풀 고갈
 *   - 모든 시나리오 지연 ↑
 *       → Tomcat 스레드 풀 고갈 (큐잉 발생)
 *   - db_writers 지연 ↑ + db_readers 지연 ↑
 *       → 쓰기 락 경합 + HikariCP 고갈 복합
 *
 * 사용법
 *   k6 run \
 *     -e BASE_URL=http://localhost:8080 \
 *     -e ACCESS_TOKEN=<JWT> \
 *     -e TEST_DATES=2026-03-01,2026-03-10,2026-03-14 \
 *     k6/connection-pool-benchmark.js
 *
 *   --출력 저장--
 *   k6 run ... --out json=result.json k6/connection-pool-benchmark.js
 * ───────────────────────────────────────────────────────────────
 */

import http  from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate, Counter } from 'k6/metrics';

// ─── Custom Metrics ────────────────────────────────────────────
// 각 레이어별로 Trend를 분리 → 출력에서 바로 비교 가능

// [Redis 레이어] 캐시 HIT 경로
const cacheHitDuration    = new Trend('layer_redis_cache_hit_ms',    true);
// [DB 레이어] 캐시 없는 순수 DB 읽기
const dbReadDuration      = new Trend('layer_db_read_ms',            true);
// [DB 레이어] 캐시 MISS 후 DB 읽기 (캐시 evict 직후)
const cacheMissDuration   = new Trend('layer_db_cache_miss_ms',      true);
// [DB 레이어] 쓰기 트랜잭션
const dbWriteDuration     = new Trend('layer_db_write_ms',           true);

// 에러 카운터 (시나리오별)
const cacheReaderErrors   = new Rate('error_cache_readers');
const dbReaderErrors      = new Rate('error_db_readers');
const cacheBusterErrors   = new Rate('error_cache_busters');
const dbWriterErrors      = new Rate('error_db_writers');

// 캐시 HIT / MISS 카운트 (비율 확인용)
const cacheHits   = new Counter('cache_hit_count');
const cacheMisses = new Counter('cache_miss_count');

// ─── 환경변수 ──────────────────────────────────────────────────
const BASE_URL = __ENV.BASE_URL || 'https://server.sdij.site';
const TOKEN    = __ENV.ACCESS_TOKEN;

if (!TOKEN) {
    throw new Error('ACCESS_TOKEN 환경변수를 설정해주세요.\n예: -e ACCESS_TOKEN=<JWT>');
}

const HEADERS = {
    'Content-Type':  'application/json',
    'Authorization': `Bearer ${TOKEN}`,
};

// ─── 날짜 풀 (class-session 조회에 사용) ───────────────────────
function buildDatePool() {
    if (__ENV.TEST_DATES) {
        return __ENV.TEST_DATES.split(',').map((d) => d.trim());
    }
    // 기본: 최근 14일 (캐시 HIT 확률을 높이려면 날짜 수를 줄일 것)
    const dates = [];
    const today = new Date();
    for (let i = 0; i < 14; i++) {
        const d = new Date(today);
        d.setDate(today.getDate() - i);
        dates.push(d.toISOString().slice(0, 10));
    }
    return dates;
}

const DATE_POOL = buildDatePool();

// ─── 랜덤 헬퍼 ────────────────────────────────────────────────
function pick(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}

function randomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

// ─── 테스트 옵션 ───────────────────────────────────────────────
export const options = {
    scenarios: {
        /**
         * 시나리오 1 — Redis 캐시 HIT 위주 읽기
         *
         * GET /api/v1/teachers          → key "teacher:all"     (TTL 30m)
         * GET /api/v1/teachers/{id}     → key "teacher:{id}"    (TTL 1h)
         * GET /api/v1/class-sessions/today → key 오늘 날짜      (TTL 12h)
         * GET /api/v1/class-sessions?date= → key 날짜          (TTL 12h)
         *
         * 두 번째 요청부터는 캐시 HIT → Redis 커넥션 집중 부하
         */
        cache_readers: {
            executor:  'ramping-vus',
            exec:      'cache_readers',
            startVUs:  0,
            stages: [
                { duration: '20s', target: 50  },  // 워밍업
                { duration: '60s', target: 100 },  // 본 부하
                { duration: '20s', target: 150 },  // 피크
                { duration: '20s', target: 0   },  // 쿨다운
            ],
            gracefulRampDown: '10s',
        },

        /**
         * 시나리오 2 — 캐시 없는 순수 DB 읽기
         *
         * GET /api/v1/classrooms        → 캐시 없음, 매번 DB 조회
         * GET /api/v1/classrooms/{id}   → 캐시 없음
         * GET /api/v1/periods           → 캐시 없음
         * GET /api/v1/counts            → 캐시 없음
         *
         * HikariCP read-only 트랜잭션 집중 부하
         */
        db_readers: {
            executor:  'ramping-vus',
            exec:      'db_readers',
            startVUs:  0,
            stages: [
                { duration: '20s', target: 30  },
                { duration: '60s', target: 60  },
                { duration: '20s', target: 80  },
                { duration: '20s', target: 0   },
            ],
            gracefulRampDown: '10s',
        },

        /**
         * 시나리오 3 — 캐시 busters (쓰기 → evict → 즉시 읽기)
         *
         * POST   /api/v1/teachers         → DB 쓰기 + cache evict "teacher:*"
         * GET    /api/v1/teachers         → 캐시 MISS → DB 읽기 → 재캐싱
         * PUT    /api/v1/teachers/{id}    → DB 쓰기 + cache evict
         * GET    /api/v1/teachers/search  → 캐시 MISS 또는 HIT
         * DELETE /api/v1/teachers/{id}   → DB 삭제 + cache evict
         *
         * 목적: evict 직후 MISS 빈도 상승 → HikariCP + Redis 동시 부하
         */
        cache_busters: {
            executor:  'ramping-vus',
            exec:      'cache_busters',
            startVUs:  0,
            stages: [
                { duration: '20s', target: 5  },
                { duration: '60s', target: 15 },
                { duration: '20s', target: 20 },
                { duration: '20s', target: 0  },
            ],
            gracefulRampDown: '10s',
        },

        /**
         * 시나리오 4 — DB 쓰기 트랜잭션 반복
         *
         * POST   /api/v1/classrooms → DB INSERT (쓰기 트랜잭션)
         * GET    /api/v1/classrooms/{id} → DB SELECT
         * DELETE /api/v1/classrooms/{id} → DB DELETE (쓰기 트랜잭션)
         *
         * 목적: HikariCP 쓰기 커넥션 점유 시간 측정
         */
        db_writers: {
            executor:  'ramping-vus',
            exec:      'db_writers',
            startVUs:  0,
            stages: [
                { duration: '20s', target: 5  },
                { duration: '60s', target: 15 },
                { duration: '20s', target: 25 },
                { duration: '20s', target: 0  },
            ],
            gracefulRampDown: '10s',
        },
    },

    // ─── 임계값: 기준을 초과하면 병목 의심 ──────────────────────
    thresholds: {
        http_req_failed:          ['rate<0.02'],          // 전체 에러율 2% 미만

        // Redis 캐시 경로
        layer_redis_cache_hit_ms: ['p(95)<200', 'p(99)<500'],   // 캐시 HIT은 빨라야 함

        // DB 읽기 경로
        layer_db_read_ms:         ['p(95)<500', 'p(99)<1000'],

        // 캐시 MISS 후 DB 읽기 (캐시 HIT보다 느린 것이 정상, 과도하면 HikariCP 고갈)
        layer_db_cache_miss_ms:   ['p(95)<800', 'p(99)<2000'],

        // DB 쓰기 트랜잭션
        layer_db_write_ms:        ['p(95)<1000', 'p(99)<3000'],

        // 시나리오별 에러율
        error_cache_readers:      ['rate<0.01'],
        error_db_readers:         ['rate<0.01'],
        error_cache_busters:      ['rate<0.05'],  // 쓰기 충돌 허용
        error_db_writers:         ['rate<0.05'],
    },
};

// ─── setup() — 테스트 데이터 준비 ─────────────────────────────
export function setup() {
    console.log('=== 커넥션 풀 병목 분석 테스트 시작 ===');
    console.log(`BASE_URL: ${BASE_URL}`);
    console.log(`날짜 풀 (${DATE_POOL.length}개): ${DATE_POOL.slice(0, 5).join(', ')} ...`);

    // 강사 목록 조회 → ID 풀 확보
    const teacherRes = http.get(`${BASE_URL}/api/v1/teachers`, { headers: HEADERS });
    let teacherIds = [];
    if (teacherRes.status === 200) {
        const body = JSON.parse(teacherRes.body);
        teacherIds = (body.teacherResponses || []).map((t) => t.id);
        console.log(`강사 ID ${teacherIds.length}개 확보`);
    } else {
        console.warn(`강사 목록 조회 실패 (status=${teacherRes.status}) — cache_busters 시나리오에서 findOne/update 생략`);
    }

    // 강의실 목록 조회 → ID 풀 확보
    const classroomRes = http.get(`${BASE_URL}/api/v1/classrooms`, { headers: HEADERS });
    let classroomIds = [];
    if (classroomRes.status === 200) {
        const body = JSON.parse(classroomRes.body);
        classroomIds = (body.classroomResponses || body.classrooms || []).map((c) => c.id);
        console.log(`강의실 ID ${classroomIds.length}개 확보`);
    }

    return { teacherIds, classroomIds };
}

// ─── 시나리오 1: cache_readers ─────────────────────────────────
export function cache_readers(data) {
    const { teacherIds } = data;

    // 1-1. GET /api/v1/teachers (전체 목록 — "teacher:all" 캐시)
    {
        const res = http.get(
            `${BASE_URL}/api/v1/teachers`,
            { headers: HEADERS, tags: { scenario: 'cache_readers', endpoint: 'teachers_all' } },
        );
        cacheHitDuration.add(res.timings.duration);

        // X-Cache-Status 헤더가 있으면 HIT/MISS 구분 (없으면 응답속도로 추정)
        const isHit = res.headers['X-Cache-Status'] === 'HIT'
            || res.timings.duration < 50; // 50ms 미만이면 캐시 HIT 추정
        isHit ? cacheHits.add(1) : cacheMisses.add(1);

        const ok = check(res, { 'teachers_all: 200': (r) => r.status === 200 });
        cacheReaderErrors.add(!ok);
    }

    sleep(0.05);

    // 1-2. GET /api/v1/teachers/{id} (단건 — "teacher:{id}" 캐시)
    if (teacherIds.length > 0) {
        const id  = pick(teacherIds);
        const res = http.get(
            `${BASE_URL}/api/v1/teachers/${id}`,
            { headers: HEADERS, tags: { scenario: 'cache_readers', endpoint: 'teachers_one' } },
        );
        cacheHitDuration.add(res.timings.duration);

        const ok = check(res, { 'teachers_one: 200': (r) => r.status === 200 });
        cacheReaderErrors.add(!ok);
    }

    sleep(0.05);

    // 1-3. GET /api/v1/class-sessions/today (오늘 — 12h 캐시)
    {
        const res = http.get(
            `${BASE_URL}/api/v1/class-sessions/today`,
            { headers: HEADERS, tags: { scenario: 'cache_readers', endpoint: 'sessions_today' } },
        );
        cacheHitDuration.add(res.timings.duration);

        const ok = check(res, { 'sessions_today: 200': (r) => r.status === 200 });
        cacheReaderErrors.add(!ok);
    }

    sleep(0.05);

    // 1-4. GET /api/v1/class-sessions?date= (날짜별 — 12h 캐시)
    {
        const date = pick(DATE_POOL);
        const res  = http.get(
            `${BASE_URL}/api/v1/class-sessions?date=${date}`,
            { headers: HEADERS, tags: { scenario: 'cache_readers', endpoint: 'sessions_by_date' } },
        );
        cacheHitDuration.add(res.timings.duration);

        const ok = check(res, { 'sessions_by_date: 200': (r) => r.status === 200 });
        cacheReaderErrors.add(!ok);
    }

    sleep(0.1);
}

// ─── 시나리오 2: db_readers ────────────────────────────────────
export function db_readers() {
    // 2-1. GET /api/v1/classrooms (캐시 없음 — 매번 DB)
    {
        const res = http.get(
            `${BASE_URL}/api/v1/classrooms`,
            { headers: HEADERS, tags: { scenario: 'db_readers', endpoint: 'classrooms_all' } },
        );
        dbReadDuration.add(res.timings.duration);

        const ok = check(res, { 'classrooms_all: 200': (r) => r.status === 200 });
        dbReaderErrors.add(!ok);
    }

    sleep(0.05);

    // 2-2. GET /api/v1/periods (캐시 없음)
    {
        const res = http.get(
            `${BASE_URL}/api/v1/periods`,
            { headers: HEADERS, tags: { scenario: 'db_readers', endpoint: 'periods_all' } },
        );
        dbReadDuration.add(res.timings.duration);

        const ok = check(res, { 'periods_all: 200': (r) => r.status === 200 });
        dbReaderErrors.add(!ok);
    }

    sleep(0.05);

    // 2-3. GET /api/v1/counts (캐시 없음)
    {
        const res = http.get(
            `${BASE_URL}/api/v1/counts`,
            { headers: HEADERS, tags: { scenario: 'db_readers', endpoint: 'counts_all' } },
        );
        dbReadDuration.add(res.timings.duration);

        const ok = check(res, { 'counts_all: 200': (r) => r.status === 200 });
        dbReaderErrors.add(!ok);
    }

    sleep(0.1);
}

// ─── 시나리오 3: cache_busters ─────────────────────────────────
export function cache_busters(data) {
    const { teacherIds } = data;
    const timestamp      = Date.now();

    // 3-1. POST /api/v1/teachers — DB 쓰기 + "teacher:*" cache evict
    const createBody = JSON.stringify({
        name:        `테스트강사_${timestamp}`,
        chalkType:   pick(['ACADEMY', 'PERSONAL', 'MIXED']),
        chalkDetail: '일반 분필',
        micType:     pick(['ACADEMY', 'PERSONAL']),
        hasPpt:      false,
        notes:       'k6 부하테스트용',
        email:       `k6_${timestamp}@test.com`,
    });

    const createRes = http.post(
        `${BASE_URL}/api/v1/teachers`,
        createBody,
        { headers: HEADERS, tags: { scenario: 'cache_busters', endpoint: 'teacher_create' } },
    );
    dbWriteDuration.add(createRes.timings.duration);

    const createOk = check(createRes, { 'teacher_create: 200 or 201': (r) => r.status === 200 || r.status === 201 });
    cacheBusterErrors.add(!createOk);

    sleep(0.1);

    // 3-2. POST 직후 GET — 캐시가 evict 됐으므로 MISS → DB 조회 → 재캐싱
    {
        const res = http.get(
            `${BASE_URL}/api/v1/teachers`,
            { headers: HEADERS, tags: { scenario: 'cache_busters', endpoint: 'teachers_after_create' } },
        );
        cacheMissDuration.add(res.timings.duration);
        cacheMisses.add(1);

        const ok = check(res, { 'teachers_after_create: 200': (r) => r.status === 200 });
        cacheBusterErrors.add(!ok);
    }

    sleep(0.05);

    // 3-3. PUT /api/v1/teachers/{id} — 기존 강사 수정 + cache evict
    if (teacherIds.length > 0) {
        const updateId   = pick(teacherIds);
        const updateBody = JSON.stringify({
            name:      `수정강사_${timestamp}`,
            chalkType: 'ACADEMY',
            micType:   'ACADEMY',
            hasPpt:    true,
        });

        const updateRes = http.put(
            `${BASE_URL}/api/v1/teachers/${updateId}`,
            updateBody,
            { headers: HEADERS, tags: { scenario: 'cache_busters', endpoint: 'teacher_update' } },
        );
        dbWriteDuration.add(updateRes.timings.duration);

        const ok = check(updateRes, { 'teacher_update: 200': (r) => r.status === 200 });
        cacheBusterErrors.add(!ok);

        sleep(0.05);

        // 3-4. 수정 직후 단건 GET — 캐시 MISS → DB 조회
        const getRes = http.get(
            `${BASE_URL}/api/v1/teachers/${updateId}`,
            { headers: HEADERS, tags: { scenario: 'cache_busters', endpoint: 'teacher_after_update' } },
        );
        cacheMissDuration.add(getRes.timings.duration);
        cacheMisses.add(1);

        check(getRes, { 'teacher_after_update: 200': (r) => r.status === 200 });
    }

    sleep(0.05);

    // 3-5. POST 에서 생성된 강사 삭제 (생성 성공 시)
    if (createOk && createRes.status === 200 || createRes.status === 201) {
        let newId = null;
        try {
            const body = JSON.parse(createRes.body);
            newId = body.id || body.teacherId || null;
        } catch { /* ignore */ }

        if (newId) {
            const deleteRes = http.del(
                `${BASE_URL}/api/v1/teachers/${newId}`,
                null,
                { headers: HEADERS, tags: { scenario: 'cache_busters', endpoint: 'teacher_delete' } },
            );
            dbWriteDuration.add(deleteRes.timings.duration);
            check(deleteRes, { 'teacher_delete: 200 or 204': (r) => r.status === 200 || r.status === 204 });
        }
    }

    sleep(0.2);
}

// ─── 시나리오 4: db_writers ────────────────────────────────────
export function db_writers() {
    const roomNumber = randomInt(700, 999); // 충돌 방지용 랜덤 호수

    // 4-1. POST /api/v1/classrooms — DB INSERT
    const createRes = http.post(
        `${BASE_URL}/api/v1/classrooms`,
        JSON.stringify({ roomNumber }),
        { headers: HEADERS, tags: { scenario: 'db_writers', endpoint: 'classroom_create' } },
    );
    dbWriteDuration.add(createRes.timings.duration);

    const createOk = check(createRes, {
        'classroom_create: 200 or 201': (r) => r.status === 200 || r.status === 201,
    });
    dbWriterErrors.add(!createOk);

    if (!createOk) {
        sleep(0.2);
        return;
    }

    // 4-2. 생성된 강의실 ID 추출
    let classroomId = null;
    try {
        const body = JSON.parse(createRes.body);
        classroomId = body.id || body.classroomId || null;
    } catch { /* ignore */ }

    sleep(0.05);

    // 4-3. GET /api/v1/classrooms/{id} — DB SELECT (캐시 없음)
    if (classroomId) {
        const getRes = http.get(
            `${BASE_URL}/api/v1/classrooms/${classroomId}`,
            { headers: HEADERS, tags: { scenario: 'db_writers', endpoint: 'classroom_get' } },
        );
        dbReadDuration.add(getRes.timings.duration);
        dbWriterErrors.add(!check(getRes, { 'classroom_get: 200': (r) => r.status === 200 }));

        sleep(0.05);

        // 4-4. DELETE /api/v1/classrooms/{id} — DB DELETE
        const deleteRes = http.del(
            `${BASE_URL}/api/v1/classrooms/${classroomId}`,
            null,
            { headers: HEADERS, tags: { scenario: 'db_writers', endpoint: 'classroom_delete' } },
        );
        dbWriteDuration.add(deleteRes.timings.duration);
        dbWriterErrors.add(!check(deleteRes, {
            'classroom_delete: 200 or 204': (r) => r.status === 200 || r.status === 204,
        }));
    }

    sleep(0.2);
}

// ─── teardown() ────────────────────────────────────────────────
export function teardown(data) {
    console.log('');
    console.log('=== 테스트 완료 — 병목 분석 가이드 ===');
    console.log('');
    console.log('[Redis 캐시 레이어]  layer_redis_cache_hit_ms 확인');
    console.log('  p95 > 200ms → Redis 연결 또는 직렬화 병목 의심');
    console.log('');
    console.log('[HikariCP 읽기]       layer_db_read_ms 확인');
    console.log('  p95 > 500ms → DB 커넥션 풀 고갈 또는 쿼리 슬로우');
    console.log('');
    console.log('[캐시 MISS → DB]     layer_db_cache_miss_ms 확인');
    console.log('  cache_hit_ms 대비 차이가 클수록 캐시 효과 큼');
    console.log('  p95 > 800ms → evict 후 thundering herd 의심');
    console.log('');
    console.log('[HikariCP 쓰기]       layer_db_write_ms 확인');
    console.log('  p95 > 1000ms → 쓰기 락 경합 또는 커넥션 풀 고갈');
    console.log('');
    console.log('[Tomcat 스레드]       http_req_duration 전체 p95 확인');
    console.log('  모든 시나리오가 동시에 지연 → Tomcat 스레드 풀 고갈');
    console.log('');
    console.log(`cache_hit_count / (cache_hit_count + cache_miss_count) 로 캐시 히트율 계산`);
}
