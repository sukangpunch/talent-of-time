/**
 * Redis 병목 확인 벤치마크 — 캐시 HIT / MISS 집중 부하
 *
 * ───────────────────────────────────────────────────────────────
 * 목적
 *   캐시 HIT 위주 읽기와 캐시 evict → MISS 연쇄로 Redis(Lettuce)
 *   커넥션 및 응답 처리량이 병목인지 확인한다.
 *
 * htop 관찰 포인트
 *   - redis-server 프로세스 CPU 사용률이 100% 근접 → Redis 처리량 한계
 *   - java(Spring) 프로세스 CPU는 낮은데 응답이 느리면
 *     → Lettuce 커넥션 대기 또는 Redis 직렬화 병목
 *
 * 판독 기준
 *   layer_redis_cache_hit_ms  p95 > 200ms  → Redis 처리 병목
 *   layer_db_cache_miss_ms    p95 >> cache_hit_ms  → evict 후 DB 재조회 부하
 *   cache_hit_count / (cache_hit_count + cache_miss_count) 로 히트율 확인
 *
 * 사용법
 *   k6 run \
 *     -e BASE_URL=http://localhost:8080 \
 *     -e ACCESS_TOKEN=<JWT> \
 *     -e TEST_DATES=2026-03-01,2026-03-10,2026-03-14 \
 *     k6/benchmark-redis.js
 * ───────────────────────────────────────────────────────────────
 */

import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate, Counter } from 'k6/metrics';

// ─── Metrics ───────────────────────────────────────────────────
const cacheHitDuration  = new Trend('layer_redis_cache_hit_ms',  true);
const cacheMissDuration = new Trend('layer_db_cache_miss_ms',    true);
const dbWriteDuration   = new Trend('layer_db_write_ms',         true);
const errorRate         = new Rate('error_rate');
const cacheHits         = new Counter('cache_hit_count');
const cacheMisses       = new Counter('cache_miss_count');

// ─── 환경변수 ──────────────────────────────────────────────────
const BASE_URL = __ENV.BASE_URL || 'https://server.sdij.site';
const TOKEN    = __ENV.ACCESS_TOKEN;

if (!TOKEN) throw new Error('ACCESS_TOKEN 환경변수를 설정해주세요. (-e ACCESS_TOKEN=<JWT>)');

const HEADERS = {
    'Content-Type':  'application/json',
    'Authorization': `Bearer ${TOKEN}`,
};

function buildDatePool() {
    if (__ENV.TEST_DATES) {
        return __ENV.TEST_DATES.split(',').map((d) => d.trim());
    }
    // 날짜 수가 적을수록 캐시 HIT 확률 ↑ (기본 7일)
    const dates = [];
    const today = new Date();
    for (let i = 0; i < 7; i++) {
        const d = new Date(today);
        d.setDate(today.getDate() - i);
        dates.push(d.toISOString().slice(0, 10));
    }
    return dates;
}

const DATE_POOL = buildDatePool();

function pick(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}

// ─── 옵션 ─────────────────────────────────────────────────────
export const options = {
    scenarios: {
        /**
         * 시나리오 A — 캐시 HIT 집중 읽기
         * 두 번째 요청부터 Redis에서 반환 → Lettuce 처리량 포화 목적
         */
        cache_readers: {
            executor:  'ramping-vus',
            exec:      'cache_readers',
            startVUs:  0,
            stages: [
                { duration: '20s', target: 50  },  // 워밍업 (캐시 적재)
                { duration: '60s', target: 120 },  // 본 부하
                { duration: '20s', target: 150 },  // 피크
                { duration: '20s', target: 0   },  // 쿨다운
            ],
            gracefulRampDown: '10s',
        },

        /**
         * 시나리오 B — 캐시 evict → MISS 연쇄
         * 쓰기로 캐시를 무효화한 직후 즉시 읽기 → MISS → DB 조회 → 재캐싱
         * Redis SET 빈도를 높여 Redis 쓰기 처리량도 함께 확인
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
    },

    thresholds: {
        http_req_failed:         ['rate<0.02'],
        error_rate:              ['rate<0.02'],
        layer_redis_cache_hit_ms: ['p(95)<200', 'p(99)<500'],
        layer_db_cache_miss_ms:   ['p(95)<800', 'p(99)<2000'],
        layer_db_write_ms:        ['p(95)<1000', 'p(99)<3000'],
    },
};

// ─── setup ────────────────────────────────────────────────────
export function setup() {
    console.log('=== Redis 병목 확인 테스트 ===');
    console.log('htop: redis-server CPU 사용률 집중 관찰');
    console.log(`날짜 풀 (${DATE_POOL.length}개): ${DATE_POOL.join(', ')}`);

    const res = http.get(`${BASE_URL}/api/v1/teachers`, { headers: HEADERS });
    let teacherIds = [];
    if (res.status === 200) {
        const body = JSON.parse(res.body);
        teacherIds = (body.teacherResponses || []).map((t) => t.id);
        console.log(`강사 ID ${teacherIds.length}개 확보`);
    }
    return { teacherIds };
}

// ─── 시나리오 A: cache_readers ─────────────────────────────────
export function cache_readers(data) {
    const { teacherIds } = data;

    // GET /api/v1/teachers — "teacher:all" 캐시 (TTL 30분)
    {
        const res = http.get(
            `${BASE_URL}/api/v1/teachers`,
            { headers: HEADERS, tags: { endpoint: 'teachers_all' } },
        );
        cacheHitDuration.add(res.timings.duration);
        res.timings.duration < 50 ? cacheHits.add(1) : cacheMisses.add(1);
        errorRate.add(!check(res, { 'teachers_all: 200': (r) => r.status === 200 }));
    }

    sleep(0.05);

    // GET /api/v1/teachers/{id} — "teacher:{id}" 캐시 (TTL 1시간)
    if (teacherIds.length > 0) {
        const res = http.get(
            `${BASE_URL}/api/v1/teachers/${pick(teacherIds)}`,
            { headers: HEADERS, tags: { endpoint: 'teachers_one' } },
        );
        cacheHitDuration.add(res.timings.duration);
        res.timings.duration < 50 ? cacheHits.add(1) : cacheMisses.add(1);
        errorRate.add(!check(res, { 'teachers_one: 200': (r) => r.status === 200 }));
    }

    sleep(0.05);

    // GET /api/v1/class-sessions/today — 오늘 날짜 캐시 (TTL 12시간)
    {
        const res = http.get(
            `${BASE_URL}/api/v1/class-sessions/today`,
            { headers: HEADERS, tags: { endpoint: 'sessions_today' } },
        );
        cacheHitDuration.add(res.timings.duration);
        res.timings.duration < 50 ? cacheHits.add(1) : cacheMisses.add(1);
        errorRate.add(!check(res, { 'sessions_today: 200': (r) => r.status === 200 }));
    }

    sleep(0.05);

    // GET /api/v1/class-sessions?date= — 날짜별 캐시 (TTL 12시간)
    {
        const res = http.get(
            `${BASE_URL}/api/v1/class-sessions?date=${pick(DATE_POOL)}`,
            { headers: HEADERS, tags: { endpoint: 'sessions_by_date' } },
        );
        cacheHitDuration.add(res.timings.duration);
        res.timings.duration < 50 ? cacheHits.add(1) : cacheMisses.add(1);
        errorRate.add(!check(res, { 'sessions_by_date: 200': (r) => r.status === 200 }));
    }

    sleep(0.1);
}

// ─── 시나리오 B: cache_busters ─────────────────────────────────
export function cache_busters(data) {
    const { teacherIds } = data;
    const timestamp      = Date.now();

    // POST → "teacher:*" 전체 evict
    const createRes = http.post(
        `${BASE_URL}/api/v1/teachers`,
        JSON.stringify({
            name:        `테스트강사_${timestamp}`,
            chalkType:   pick(['ACADEMY', 'PERSONAL', 'MIXED']),
            chalkDetail: '일반 분필',
            micType:     pick(['ACADEMY', 'PERSONAL']),
            hasPpt:      false,
            notes:       'k6 Redis 병목 테스트용',
            email:       `k6_${timestamp}@test.com`,
        }),
        { headers: HEADERS, tags: { endpoint: 'teacher_create' } },
    );
    dbWriteDuration.add(createRes.timings.duration);
    const createOk = check(createRes, { 'teacher_create: 2xx': (r) => r.status === 200 || r.status === 201 });
    errorRate.add(!createOk);

    sleep(0.05);

    // evict 직후 GET → 캐시 MISS → DB 조회 → Redis SET (재캐싱)
    {
        const res = http.get(
            `${BASE_URL}/api/v1/teachers`,
            { headers: HEADERS, tags: { endpoint: 'teachers_after_evict' } },
        );
        cacheMissDuration.add(res.timings.duration);
        cacheMisses.add(1);
        errorRate.add(!check(res, { 'teachers_after_evict: 200': (r) => r.status === 200 }));
    }

    sleep(0.05);

    // 기존 강사 수정 → 단건 캐시 evict → 즉시 단건 GET (MISS)
    if (teacherIds.length > 0) {
        const updateId = pick(teacherIds);
        const updateRes = http.put(
            `${BASE_URL}/api/v1/teachers/${updateId}`,
            JSON.stringify({ name: `수정강사_${timestamp}`, chalkType: 'ACADEMY', micType: 'ACADEMY', hasPpt: true }),
            { headers: HEADERS, tags: { endpoint: 'teacher_update' } },
        );
        dbWriteDuration.add(updateRes.timings.duration);
        errorRate.add(!check(updateRes, { 'teacher_update: 200': (r) => r.status === 200 }));

        sleep(0.05);

        const getRes = http.get(
            `${BASE_URL}/api/v1/teachers/${updateId}`,
            { headers: HEADERS, tags: { endpoint: 'teacher_after_update' } },
        );
        cacheMissDuration.add(getRes.timings.duration);
        cacheMisses.add(1);
        check(getRes, { 'teacher_after_update: 200': (r) => r.status === 200 });
    }

    sleep(0.05);

    // 생성된 강사 삭제 (정리)
    if (createOk) {
        let newId = null;
        try { newId = JSON.parse(createRes.body).id || JSON.parse(createRes.body).teacherId; } catch { /**/ }
        if (newId) {
            const deleteRes = http.del(
                `${BASE_URL}/api/v1/teachers/${newId}`,
                null,
                { headers: HEADERS, tags: { endpoint: 'teacher_delete' } },
            );
            dbWriteDuration.add(deleteRes.timings.duration);
            check(deleteRes, { 'teacher_delete: 2xx': (r) => r.status === 200 || r.status === 204 });
        }
    }

    sleep(0.2);
}

// ─── teardown ─────────────────────────────────────────────────
export function teardown() {
    console.log('');
    console.log('=== Redis 병목 분석 결과 판독 가이드 ===');
    console.log('layer_redis_cache_hit_ms p95 > 200ms  → Redis 처리량 병목');
    console.log('layer_db_cache_miss_ms   p95 > 800ms  → evict 후 DB 재조회 과부하 (thundering herd 의심)');
    console.log('cache_hit_count / (cache_hit_count + cache_miss_count) → 캐시 히트율 확인');
}
