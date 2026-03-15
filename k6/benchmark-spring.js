/**
 * Spring 서버(Tomcat 스레드 풀) 병목 확인 벤치마크
 *
 * ───────────────────────────────────────────────────────────────
 * 목적
 *   Tomcat 스레드 풀이 먼저 포화되는지 확인한다.
 *   모든 엔드포인트(캐시 HIT 포함)에 최대한 많은 동시 요청을 보내고,
 *   모든 레이어의 지연이 동시에 올라가는지 관찰한다.
 *
 * htop 관찰 포인트
 *   - java(Spring) 프로세스 CPU가 100% 근접하고
 *     redis-server · mysqld CPU는 낮다
 *     → Tomcat 스레드 풀 고갈 (server.tomcat.threads.max 확인)
 *   - http_req_waiting (서버 처리 대기) 지표가 http_req_duration의
 *     대부분을 차지한다면 → 큐잉(queuing) 발생 확정
 *
 * 판독 기준
 *   http_req_duration p95 가 모든 엔드포인트에서 동시에 상승
 *   → 특정 레이어(Redis·DB)가 아닌 서버 스레드 부족
 *
 * 사용법
 *   k6 run \
 *     -e BASE_URL=http://localhost:8080 \
 *     -e ACCESS_TOKEN=<JWT> \
 *     -e TEST_DATES=2026-03-01,2026-03-10,2026-03-14 \
 *     k6/benchmark-spring.js
 * ───────────────────────────────────────────────────────────────
 */

import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate } from 'k6/metrics';

// ─── Metrics ───────────────────────────────────────────────────
// 각 엔드포인트 그룹별 응답시간 — 동시에 올라가면 Tomcat 병목
const cachedEndpointDuration  = new Trend('spring_cached_endpoint_ms',   true);
const uncachedEndpointDuration = new Trend('spring_uncached_endpoint_ms', true);
const errorRate                = new Rate('error_rate');

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
         * Tomcat 스레드 포화 시나리오
         *
         * 캐시 HIT 엔드포인트(Redis에서 즉시 반환)와
         * 캐시 없는 DB 엔드포인트를 섞어서 대량 요청을 보낸다.
         *
         * 캐시 HIT임에도 불구하고 응답이 느려지면 →
         * Redis·DB가 아닌 Tomcat 스레드가 병목임을 의미한다.
         *
         * VU를 단계적으로 높여 Tomcat 기본 스레드 수(200)를
         * 초과하는 시점에서 지연이 급격히 오르는지 확인한다.
         */
        tomcat_saturation: {
            executor:  'ramping-vus',
            exec:      'tomcat_saturation',
            startVUs:  0,
            stages: [
                { duration: '15s', target: 50  },  // 워밍업
                { duration: '30s', target: 100 },  // Tomcat 기본값(200) 이전
                { duration: '30s', target: 200 },  // Tomcat 스레드 수 한계 도달
                { duration: '30s', target: 300 },  // 초과 → 큐잉 시작 예상
                { duration: '15s', target: 0   },  // 쿨다운
            ],
            gracefulRampDown: '10s',
        },
    },

    thresholds: {
        http_req_failed:           ['rate<0.05'],
        error_rate:                ['rate<0.05'],
        // 두 지표가 동시에 이 기준을 넘으면 Tomcat 병목
        spring_cached_endpoint_ms:   ['p(95)<500', 'p(99)<1000'],
        spring_uncached_endpoint_ms: ['p(95)<500', 'p(99)<1000'],
    },
};

// ─── setup ────────────────────────────────────────────────────
export function setup() {
    console.log('=== Spring 서버(Tomcat) 병목 확인 테스트 ===');
    console.log('htop: java 프로세스 CPU 사용률 집중 관찰');
    console.log('VU 200 초과 시점에서 http_req_waiting 급등 여부 확인');

    const res = http.get(`${BASE_URL}/api/v1/teachers`, { headers: HEADERS });
    let teacherIds = [];
    if (res.status === 200) {
        const body = JSON.parse(res.body);
        teacherIds = (body.teacherResponses || []).map((t) => t.id);
        console.log(`강사 ID ${teacherIds.length}개 확보`);
    }
    return { teacherIds };
}

// ─── 시나리오: tomcat_saturation ──────────────────────────────
export function tomcat_saturation(data) {
    const { teacherIds } = data;

    // ── 캐시 HIT 엔드포인트 (Redis에서 즉시 반환) ──────────────
    // 이 그룹이 느려지면 Tomcat 스레드 병목을 의심

    const teachersRes = http.get(
        `${BASE_URL}/api/v1/teachers`,
        { headers: HEADERS, tags: { group: 'cached' } },
    );
    cachedEndpointDuration.add(teachersRes.timings.duration);
    errorRate.add(!check(teachersRes, { 'teachers_all: 200': (r) => r.status === 200 }));

    // ── 캐시 없는 엔드포인트 (매번 DB 조회) ───────────────────
    // 이 그룹은 원래 느린 것이 정상 — 비교 기준

    const classroomsRes = http.get(
        `${BASE_URL}/api/v1/classrooms`,
        { headers: HEADERS, tags: { group: 'uncached' } },
    );
    uncachedEndpointDuration.add(classroomsRes.timings.duration);
    errorRate.add(!check(classroomsRes, { 'classrooms_all: 200': (r) => r.status === 200 }));

    // sleep 최소화 → 스레드를 최대한 빨리 점유
    sleep(0.02);

    const sessionsRes = http.get(
        `${BASE_URL}/api/v1/class-sessions/today`,
        { headers: HEADERS, tags: { group: 'cached' } },
    );
    cachedEndpointDuration.add(sessionsRes.timings.duration);
    errorRate.add(!check(sessionsRes, { 'sessions_today: 200': (r) => r.status === 200 }));

    const periodsRes = http.get(
        `${BASE_URL}/api/v1/periods`,
        { headers: HEADERS, tags: { group: 'uncached' } },
    );
    uncachedEndpointDuration.add(periodsRes.timings.duration);
    errorRate.add(!check(periodsRes, { 'periods_all: 200': (r) => r.status === 200 }));

    sleep(0.02);

    if (teacherIds.length > 0) {
        const res = http.get(
            `${BASE_URL}/api/v1/teachers/${pick(teacherIds)}`,
            { headers: HEADERS, tags: { group: 'cached' } },
        );
        cachedEndpointDuration.add(res.timings.duration);
        errorRate.add(!check(res, { 'teachers_one: 200': (r) => r.status === 200 }));
    }

    const dateRes = http.get(
        `${BASE_URL}/api/v1/class-sessions?date=${pick(DATE_POOL)}`,
        { headers: HEADERS, tags: { group: 'cached' } },
    );
    cachedEndpointDuration.add(dateRes.timings.duration);
    errorRate.add(!check(dateRes, { 'sessions_by_date: 200': (r) => r.status === 200 }));

    sleep(0.02);
}

// ─── teardown ─────────────────────────────────────────────────
export function teardown() {
    console.log('');
    console.log('=== Tomcat 병목 분석 결과 판독 가이드 ===');
    console.log('spring_cached_endpoint_ms 와 spring_uncached_endpoint_ms 가');
    console.log('동시에 p95 > 500ms 로 상승 → Tomcat 스레드 풀 고갈');
    console.log('');
    console.log('캐시 HIT(spring_cached_endpoint_ms)만 빠르고');
    console.log('uncached만 느리면 → Tomcat은 정상, HikariCP 병목 의심');
    console.log('→ benchmark-db.js 로 확인');
}
