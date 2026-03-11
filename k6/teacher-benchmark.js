/**
 * Teacher API 성능 벤치마크
 *
 * 사용법:
 *   k6 run -e BASE_URL=https://your-server.com -e ACCESS_TOKEN=your_jwt_token teacher-benchmark.js
 *
 * 옵션 오버라이드:
 *   k6 run --vus 100 --duration 30s -e BASE_URL=... -e ACCESS_TOKEN=... teacher-benchmark.js
 */

import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate } from 'k6/metrics';

// ---------------------------------------------------------------------------
// Custom Metrics — 엔드포인트별 응답시간을 분리해서 비교
// ---------------------------------------------------------------------------
const findAllDuration   = new Trend('teacher_find_all_duration',    true); // true = 히스토그램 출력
const findOneDuration   = new Trend('teacher_find_one_duration',    true);
const searchDuration    = new Trend('teacher_search_duration',      true);
const errorRate         = new Rate('teacher_error_rate');

// ---------------------------------------------------------------------------
// 테스트 옵션
// ---------------------------------------------------------------------------
export const options = {
    scenarios: {
        teacher_load: {
            executor:  'constant-vus',
            vus:       100,
            duration:  '30s',
        },
    },
    thresholds: {
        // 전체 요청 실패율 1% 미만
        http_req_failed:           ['rate<0.01'],
        teacher_error_rate:        ['rate<0.01'],

        // 엔드포인트별 p95 응답시간 기준 (캐싱 후에는 이 값이 크게 낮아져야 함)
        teacher_find_all_duration: ['p(95)<1000', 'p(99)<2000'],
        teacher_find_one_duration: ['p(95)<500',  'p(99)<1000'],
        teacher_search_duration:   ['p(95)<500',  'p(99)<1000'],
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

// 검색에 사용할 이름 접두사 목록
const SEARCH_PREFIXES = ['김', '이', '박', '최', '정'];

// ---------------------------------------------------------------------------
// setup() — 테스트 시작 전 1회 실행, 실제 teacher ID 목록을 서버에서 가져옴
// ---------------------------------------------------------------------------
export function setup() {
    const res = http.get(`${BASE_URL}/api/v1/teachers`, { headers: HEADERS });

    check(res, { 'setup: 선생님 목록 조회 성공': (r) => r.status === 200 });

    if (res.status !== 200) {
        console.error(`setup 실패 (status=${res.status}): 선생님 목록을 가져올 수 없습니다.`);
        return { teacherIds: [] };
    }

    const teachers  = JSON.parse(res.body);
    const teacherIds = teachers.map((t) => t.id);

    console.log(`setup 완료: 선생님 ${teacherIds.length}명의 ID를 확인했습니다.`);
    return { teacherIds };
}

// ---------------------------------------------------------------------------
// default() — 100 VU가 30초 동안 반복 실행
// ---------------------------------------------------------------------------
export default function (data) {
    const { teacherIds } = data;

    if (teacherIds.length === 0) {
        console.warn('teacher ID가 없어 findOne / search 테스트를 건너뜁니다.');
        return;
    }

    const randomId     = teacherIds[Math.floor(Math.random() * teacherIds.length)];
    const randomPrefix = SEARCH_PREFIXES[Math.floor(Math.random() * SEARCH_PREFIXES.length)];

    // ------------------------------------------------------------------
    // 1. GET /api/v1/teachers — 전체 선생님 목록 조회
    // ------------------------------------------------------------------
    {
        const res = http.get(
            `${BASE_URL}/api/v1/teachers`,
            { headers: HEADERS, tags: { endpoint: 'find_all' } },
        );
        findAllDuration.add(res.timings.duration);
        const ok = check(res, { 'find_all: status 200': (r) => r.status === 200 });
        errorRate.add(!ok);
    }

    sleep(0.05);

    // ------------------------------------------------------------------
    // 2. GET /api/v1/teachers/{id} — 단건 선생님 조회
    // ------------------------------------------------------------------
    {
        const res = http.get(
            `${BASE_URL}/api/v1/teachers/${randomId}`,
            { headers: HEADERS, tags: { endpoint: 'find_one' } },
        );
        findOneDuration.add(res.timings.duration);
        const ok = check(res, { 'find_one: status 200': (r) => r.status === 200 });
        errorRate.add(!ok);
    }

    sleep(0.05);

    // ------------------------------------------------------------------
    // 3. GET /api/v1/teachers/search?name= — 이름 접두사 검색
    // ------------------------------------------------------------------
    {
        const res = http.get(
            `${BASE_URL}/api/v1/teachers/search?name=${encodeURIComponent(randomPrefix)}`,
            { headers: HEADERS, tags: { endpoint: 'search' } },
        );
        searchDuration.add(res.timings.duration);
        const ok = check(res, { 'search: status 200': (r) => r.status === 200 });
        errorRate.add(!ok);
    }

    sleep(0.1);
}

// ---------------------------------------------------------------------------
// teardown() — 테스트 종료 후 1회 실행
// ---------------------------------------------------------------------------
export function teardown(data) {
    console.log(`테스트 완료. 측정에 사용된 teacher ID 수: ${data.teacherIds.length}`);
}
