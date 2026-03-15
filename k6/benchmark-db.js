/**
 * DB(HikariCP) 병목 확인 벤치마크 — 순수 DB 읽기 · 쓰기 집중 부하
 *
 * ───────────────────────────────────────────────────────────────
 * 목적
 *   캐시를 우회한 DB 직접 조회와 쓰기 트랜잭션을 집중적으로 발생시켜
 *   HikariCP 커넥션 풀이 병목인지 확인한다.
 *
 * htop 관찰 포인트
 *   - mysqld 프로세스 CPU 사용률 상승
 *     → 쿼리 자체가 무거운 경우 (인덱스 미스 등)
 *   - mysqld CPU는 낮은데 응답이 느리면
 *     → HikariCP 커넥션 대기 (커넥션 풀 고갈)
 *   - java(Spring) CPU도 낮고 mysqld도 낮은데 느리면
 *     → 커넥션 checkout timeout 대기 상태
 *     → hikari.maximum-pool-size 증가로 해소 가능
 *
 * 판독 기준
 *   layer_db_read_ms  p95 > 500ms   → HikariCP 읽기 커넥션 고갈
 *   layer_db_write_ms p95 > 1000ms  → HikariCP 쓰기 커넥션 고갈 또는 락 경합
 *   두 지표가 동시에 상승하면 풀 크기 자체가 부족한 것
 *
 * 사용법
 *   k6 run \
 *     -e BASE_URL=http://localhost:8080 \
 *     -e ACCESS_TOKEN=<JWT> \
 *     k6/benchmark-db.js
 * ───────────────────────────────────────────────────────────────
 */

import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate } from 'k6/metrics';

// ─── Metrics ───────────────────────────────────────────────────
const dbReadDuration  = new Trend('layer_db_read_ms',  true);
const dbWriteDuration = new Trend('layer_db_write_ms', true);
const readerErrors    = new Rate('error_db_readers');
const writerErrors    = new Rate('error_db_writers');

// ─── 환경변수 ──────────────────────────────────────────────────
const BASE_URL = __ENV.BASE_URL || 'https://server.sdij.site';
const TOKEN    = __ENV.ACCESS_TOKEN;

if (!TOKEN) throw new Error('ACCESS_TOKEN 환경변수를 설정해주세요. (-e ACCESS_TOKEN=<JWT>)');

const HEADERS = {
    'Content-Type':  'application/json',
    'Authorization': `Bearer ${TOKEN}`,
};

function pick(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}

function randomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

// ─── 옵션 ─────────────────────────────────────────────────────
export const options = {
    scenarios: {
        /**
         * 시나리오 A — 캐시 없는 순수 DB 읽기
         *
         * 캐시가 없는 엔드포인트만 선택 → HikariCP read-only 트랜잭션 포화
         * VU를 점진적으로 늘려 커넥션 풀 고갈 시점 확인
         */
        db_readers: {
            executor:  'ramping-vus',
            exec:      'db_readers',
            startVUs:  0,
            stages: [
                { duration: '20s', target: 20  },
                { duration: '40s', target: 60  },
                { duration: '20s', target: 100 },  // HikariCP 기본 max(10) 10배
                { duration: '20s', target: 0   },
            ],
            gracefulRampDown: '10s',
        },

        /**
         * 시나리오 B — DB 쓰기 트랜잭션 반복 (POST → GET → DELETE)
         *
         * 쓰기 트랜잭션은 커넥션 점유 시간이 읽기보다 길다.
         * 쓰기 VU가 늘어날수록 읽기 VU도 커넥션 대기에 걸리는지 확인.
         */
        db_writers: {
            executor:  'ramping-vus',
            exec:      'db_writers',
            startVUs:  0,
            stages: [
                { duration: '20s', target: 5  },
                { duration: '40s', target: 20 },
                { duration: '20s', target: 30 },  // 읽기와 쓰기가 풀을 동시에 경합
                { duration: '20s', target: 0  },
            ],
            gracefulRampDown: '10s',
        },
    },

    thresholds: {
        http_req_failed:    ['rate<0.05'],
        error_db_readers:   ['rate<0.02'],
        error_db_writers:   ['rate<0.05'],
        layer_db_read_ms:   ['p(95)<500',  'p(99)<1000'],
        layer_db_write_ms:  ['p(95)<1000', 'p(99)<3000'],
    },
};

// ─── setup ────────────────────────────────────────────────────
export function setup() {
    console.log('=== DB(HikariCP) 병목 확인 테스트 ===');
    console.log('htop: mysqld 프로세스 CPU 및 I/O 집중 관찰');
    console.log('mysqld CPU 낮고 응답 느리면 → HikariCP 커넥션 풀 고갈 의심');

    // 강의실 목록 확보 (db_readers에서 단건 조회용)
    const res = http.get(`${BASE_URL}/api/v1/classrooms`, { headers: HEADERS });
    let classroomIds = [];
    if (res.status === 200) {
        const body = JSON.parse(res.body);
        classroomIds = (body.classroomResponses || body.classrooms || []).map((c) => c.id);
        console.log(`강의실 ID ${classroomIds.length}개 확보`);
    }
    return { classroomIds };
}

// ─── 시나리오 A: db_readers ────────────────────────────────────
export function db_readers(data) {
    const { classroomIds } = data;

    // GET /api/v1/classrooms — 캐시 없음, 매번 DB SELECT
    {
        const res = http.get(
            `${BASE_URL}/api/v1/classrooms`,
            { headers: HEADERS, tags: { endpoint: 'classrooms_all' } },
        );
        dbReadDuration.add(res.timings.duration);
        readerErrors.add(!check(res, { 'classrooms_all: 200': (r) => r.status === 200 }));
    }

    sleep(0.05);

    // GET /api/v1/classrooms/{id} — 단건 DB SELECT
    if (classroomIds.length > 0) {
        const res = http.get(
            `${BASE_URL}/api/v1/classrooms/${pick(classroomIds)}`,
            { headers: HEADERS, tags: { endpoint: 'classrooms_one' } },
        );
        dbReadDuration.add(res.timings.duration);
        readerErrors.add(!check(res, { 'classrooms_one: 200': (r) => r.status === 200 }));
    }

    sleep(0.05);

    // GET /api/v1/periods — 캐시 없음
    {
        const res = http.get(
            `${BASE_URL}/api/v1/periods`,
            { headers: HEADERS, tags: { endpoint: 'periods_all' } },
        );
        dbReadDuration.add(res.timings.duration);
        readerErrors.add(!check(res, { 'periods_all: 200': (r) => r.status === 200 }));
    }

    sleep(0.05);

    // GET /api/v1/counts — 캐시 없음
    {
        const res = http.get(
            `${BASE_URL}/api/v1/counts`,
            { headers: HEADERS, tags: { endpoint: 'counts_all' } },
        );
        dbReadDuration.add(res.timings.duration);
        readerErrors.add(!check(res, { 'counts_all: 200': (r) => r.status === 200 }));
    }

    sleep(0.1);
}

// ─── 시나리오 B: db_writers ────────────────────────────────────
export function db_writers() {
    const roomNumber = randomInt(1000, 9999); // 충돌 방지

    // POST /api/v1/classrooms — DB INSERT (쓰기 트랜잭션 시작)
    const createRes = http.post(
        `${BASE_URL}/api/v1/classrooms`,
        JSON.stringify({ roomNumber }),
        { headers: HEADERS, tags: { endpoint: 'classroom_create' } },
    );
    dbWriteDuration.add(createRes.timings.duration);
    const createOk = check(createRes, {
        'classroom_create: 2xx': (r) => r.status === 200 || r.status === 201,
    });
    writerErrors.add(!createOk);

    if (!createOk) {
        sleep(0.2);
        return;
    }

    let classroomId = null;
    try {
        const body = JSON.parse(createRes.body);
        classroomId = body.id || body.classroomId || null;
    } catch { /**/ }

    sleep(0.05);

    if (classroomId) {
        // GET /api/v1/classrooms/{id} — INSERT 직후 SELECT (같은 커넥션 풀 경합)
        const getRes = http.get(
            `${BASE_URL}/api/v1/classrooms/${classroomId}`,
            { headers: HEADERS, tags: { endpoint: 'classroom_get' } },
        );
        dbReadDuration.add(getRes.timings.duration);
        writerErrors.add(!check(getRes, { 'classroom_get: 200': (r) => r.status === 200 }));

        sleep(0.05);

        // DELETE /api/v1/classrooms/{id} — DB DELETE (쓰기 트랜잭션)
        const deleteRes = http.del(
            `${BASE_URL}/api/v1/classrooms/${classroomId}`,
            null,
            { headers: HEADERS, tags: { endpoint: 'classroom_delete' } },
        );
        dbWriteDuration.add(deleteRes.timings.duration);
        writerErrors.add(!check(deleteRes, {
            'classroom_delete: 2xx': (r) => r.status === 200 || r.status === 204,
        }));
    }

    sleep(0.2);
}

// ─── teardown ─────────────────────────────────────────────────
export function teardown() {
    console.log('');
    console.log('=== DB(HikariCP) 병목 분석 결과 판독 가이드 ===');
    console.log('layer_db_read_ms  p95 > 500ms   → HikariCP 읽기 커넥션 고갈');
    console.log('layer_db_write_ms p95 > 1000ms  → HikariCP 쓰기 커넥션 고갈 또는 락 경합');
    console.log('두 지표 동시 상승 → maximum-pool-size 증가 검토');
    console.log('');
    console.log('mysqld CPU 낮고 지연 발생 → 커넥션 checkout timeout 대기');
    console.log('mysqld CPU 높고 지연 발생 → 쿼리 최적화(인덱스) 필요');
}
