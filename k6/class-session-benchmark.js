/**
 * ClassSession API 성능 벤치마크 — findClassSessionsByDate
 *
 * 사용법:
 *   k6 run \
 *     -e BASE_URL=https://your-server.com \
 *     -e ACCESS_TOKEN=your_jwt_token \
 *     -e TEST_DATES=2026-03-01,2026-03-05,2026-03-10,2026-03-11 \
 *     k6/class-session-benchmark.js
 *
 * TEST_DATES: 실제 데이터가 있는 날짜를 콤마로 구분해서 전달
 *             생략 시 오늘부터 최근 30일을 자동 생성 (빈 결과 반환 가능)
 */

import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate } from 'k6/metrics';

// ---------------------------------------------------------------------------
// Custom Metrics
// ---------------------------------------------------------------------------
const findByDateDuration = new Trend('class_session_find_by_date_duration', true);
const errorRate          = new Rate('class_session_error_rate');

// ---------------------------------------------------------------------------
// 테스트 옵션
// ---------------------------------------------------------------------------
export const options = {
    scenarios: {
        class_session_load: {
            executor: 'constant-vus',
            vus:      100,
            duration: '30s',
        },
    },
    thresholds: {
        http_req_failed:                   ['rate<0.01'],
        class_session_error_rate:          ['rate<0.01'],
        class_session_find_by_date_duration: ['p(95)<1000', 'p(99)<2000'],
    },
};

// ---------------------------------------------------------------------------
// 환경변수
// ---------------------------------------------------------------------------
const BASE_URL = __ENV.BASE_URL;
const TOKEN    = __ENV.ACCESS_TOKEN;

if (!BASE_URL) {
    throw new Error('BASE_URL 환경변수를 설정해주세요. (-e BASE_URL=https://your-server.com)');
}
if (!TOKEN) {
    throw new Error('ACCESS_TOKEN 환경변수를 설정해주세요. (-e ACCESS_TOKEN=your_jwt_token)');
}

const HEADERS = {
    'Content-Type':  'application/json',
    'Authorization': `Bearer ${TOKEN}`,
};

/**
 * TEST_DATES 환경변수가 없으면 오늘부터 최근 30일을 자동 생성
 * 실제 데이터가 있는 날짜를 -e TEST_DATES= 로 넘기는 것을 권장
 */
function buildDatePool() {
    if (__ENV.TEST_DATES) {
        return __ENV.TEST_DATES.split(',').map((d) => d.trim());
    }

    const dates = [];
    const today = new Date();
    for (let i = 0; i < 30; i++) {
        const d = new Date(today);
        d.setDate(today.getDate() - i);
        dates.push(d.toISOString().slice(0, 10)); // YYYY-MM-DD
    }
    return dates;
}

const DATE_POOL = buildDatePool();

// ---------------------------------------------------------------------------
// setup() — 날짜 풀 확인 로그
// ---------------------------------------------------------------------------
export function setup() {
    console.log(`테스트 날짜 풀 (${DATE_POOL.length}개): ${DATE_POOL.slice(0, 5).join(', ')} ...`);

    // 첫 번째 날짜로 사전 확인
    const sampleDate = DATE_POOL[0];
    const res = http.get(
        `${BASE_URL}/api/v1/class-sessions?date=${sampleDate}`,
        { headers: HEADERS },
    );

    check(res, { 'setup: 날짜 조회 성공': (r) => r.status === 200 });

    if (res.status === 200) {
        const body = JSON.parse(res.body);
        console.log(`setup 확인 (date=${sampleDate}): ${body.classSessionResponses.length}건 반환`);
    } else {
        console.warn(`setup 경고: date=${sampleDate} 조회 실패 (status=${res.status})`);
    }
}

// ---------------------------------------------------------------------------
// default() — 100 VU가 30초 동안 반복 실행
// ---------------------------------------------------------------------------
export default function () {
    // 날짜 풀에서 무작위 선택 → 캐시 hit/miss 혼합 시나리오 재현
    const date = DATE_POOL[Math.floor(Math.random() * DATE_POOL.length)];

    const res = http.get(
        `${BASE_URL}/api/v1/class-sessions?date=${date}`,
        {
            headers: HEADERS,
            tags:    { endpoint: 'find_by_date', date },
        },
    );

    findByDateDuration.add(res.timings.duration);

    const ok = check(res, {
        'find_by_date: status 200':    (r) => r.status === 200,
        'find_by_date: body has classSessionResponses': (r) => {
            try { return Array.isArray(JSON.parse(r.body).classSessionResponses); } catch { return false; }
        },
    });
    errorRate.add(!ok);

    sleep(0.1);
}
