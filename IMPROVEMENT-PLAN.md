# Claude Code 활용 구조 개선 플랜

## Context

REFACTORING.md에 정리된 Claude Code 강의 내용을 바탕으로 현재 프로젝트의 AI 활용 구조를 개선한다.
현재는 MCP(Serena), CLAUDE.MD, Skills(test), Agents 2개, Hooks(코드 재점검)를 사용 중이지만,
개선 전후 성능 수치 비교 없이 구성한 상태다.

**목표:** 개선 전/후 성능(컨텍스트 사용량, 토큰 사용량, 답변 품질)을 수치로 비교한다.

---

## Phase 0: Baseline 측정 (개선 전 측정)

### 측정 방법
Claude Code의 `/context` 명령어로 각 시나리오 전후 컨텍스트 사용량을 캡처한다.

### 측정 시나리오 (5가지 표준 작업)

| 번호 | 시나리오 | 측정 지표 |
|------|---------|---------|
| S1 | 새 도메인 기능 추가 (예: "schedule에 필드 추가해줘") | 세션 시작 ~ 작업 완료 총 토큰 |
| S2 | 버그 수정 (에러 로그 붙여넣기 후 수정) | 컨텍스트 윈도우 % 소진량 |
| S3 | 리팩토링 요청 (ScheduleService 메서드 분리) | 필요 없는 파일 읽기 횟수 |
| S4 | 테스트 코드 작성 (/test skill 사용) | 완료까지 메시지 교환 횟수 |
| S5 | 아키텍처 질문 ("캐싱 구조 설명해줘") | 답변 품질 점수 (1-5) |

### 기록 형식 (docs/before.md 파일로 저장)
```
시나리오: S1
시작 컨텍스트: X tokens / Y%
종료 컨텍스트: X tokens / Y%
사용 토큰: X
메시지 교환 수: X
답변 품질: X/5
비고:
```

---

## Phase 1: CLAUDE.md Lazy Loading 최적화

### 현재 문제
- `CLAUDE.md` 가 100+ 라인으로 매 세션마다 전체 로드됨
- 아키텍처 설명, 컨벤션, 명령어 등 모든 내용이 단일 파일에 혼재

### 개선 방향
CLAUDE.md를 "참조 인덱스"로 축소하고, 세부 내용은 별도 파일로 분리

**수정 파일:** `CLAUDE.md` (현재 100+ 라인 → 30라인 이하로 축소)

**신규 파일:**
- `docs/conventions.md` - 코드 스타일, 네이밍 컨벤션
- `docs/architecture.md` - 아키텍처 다이어그램 (Mermaid)
- `docs/workflow.md` - 개발 워크플로우, Git 컨벤션

**CLAUDE.md 최종 구조:**
```markdown
# talent-of-time
[프로젝트 한 줄 요약]

## 빠른 참조
- 컨벤션: @docs/conventions.md
- 아키텍처: @docs/architecture.md
- 워크플로우: @docs/workflow.md

## 핵심만
- Java 21, Spring Boot 3.3.5, MySQL, Redis
- 빌드: gradlew.bat build / test / bootRun
- 구조: Controller → Service → Repository → Domain
```

---

## Phase 2: 도메인별 CLAUDE.md 추가

### 개선 방향
각 도메인 폴더에 해당 도메인의 비즈니스 규칙만 담은 CLAUDE.md 추가.
Claude가 특정 도메인 작업 시 전체 프로젝트 컨텍스트 대신 해당 도메인 컨텍스트만 로드.

**신규 파일 목록:**
```
src/main/java/com/example/talentoftime/schedule/CLAUDE.md
  → 스케줄 배정 알고리즘, count 기반 공평 분배 규칙

src/main/java/com/example/talentoftime/classsession/CLAUDE.md
  → 캐싱 전략 (@ThunderingHerdCaching), Gemini 이미지 파싱

src/main/java/com/example/talentoftime/auth/CLAUDE.md
  → JWT 발급 흐름, Kakao OAuth 연동, 토큰 갱신 전략

src/main/java/com/example/talentoftime/cache/CLAUDE.md
  → 커스텀 캐시 어노테이션 사용법, TTL 규칙
```

---

## Phase 3: Mermaid 아키텍처 다이어그램 추가

### 개선 방향
`docs/architecture.md`에 Mermaid 다이어그램으로 아키텍처를 시각화.
텍스트 설명 대신 다이어그램으로 컨텍스트 효율 향상.

**포함할 다이어그램:**
1. 전체 시스템 흐름 (클라이언트 → 서버 → DB/Redis)
2. 스케줄 자동 배정 로직 플로우차트
3. 인증 흐름 (OAuth2 + JWT)
4. 캐싱 전략 다이어그램

---

## Phase 4: 스크립트 오프로드

### 개선 방향
컨텍스트를 오염시키는 무거운 작업을 별도 스크립트로 분리.

**신규 스크립트:**
```
scripts/
├── check-db-state.sh    # DB 상태 확인 (결과만 출력)
├── analyze-logs.sh      # 로그 분석 (요약만 반환)
├── api-test.sh          # API 응답 비교 테스트
└── count-reset.sh       # count 초기화 검증
```

각 스크립트는 결과를 간결하게 출력하여 Claude에게 전달하는 용도.

---

## Phase 5: After 측정 및 비교

### Phase 0와 동일한 5개 시나리오를 재실행
`docs/performance-comparison.md` 파일에 결과 기록:

| 시나리오 | Before 토큰 | After 토큰 | 절감률 | Before 품질 | After 품질 |
|---------|------------|-----------|-------|------------|-----------|
| S1      |            |           |       |            |           |
| S2      |            |           |       |            |           |
| S3      |            |           |       |            |           |
| S4      |            |           |       |            |           |
| S5      |            |           |       |            |           |

---

## 수행 순서 요약

1. **[측정]** S1~S5 시나리오 실행 → `docs/before.md`에 수치 기록
2. **[구현]** CLAUDE.md Lazy Loading 적용 (`CLAUDE.md` 수정, `docs/` 파일 생성)
3. **[구현]** 도메인별 CLAUDE.md 4개 생성
4. **[구현]** `docs/architecture.md`에 Mermaid 다이어그램 작성
5. **[구현]** `scripts/` 폴더에 오프로드 스크립트 생성
6. **[측정]** 동일 S1~S5 재실행 → `docs/performance-comparison.md` 작성

---

## 수정/생성 파일 목록

| 파일 | 작업 |
|------|------|
| `CLAUDE.md` | 수정 (축소) |
| `docs/conventions.md` | 신규 |
| `docs/architecture.md` | 신규 (Mermaid 포함) |
| `docs/workflow.md` | 신규 |
| `docs/before.md` | 신규 (측정 기록) |
| `docs/performance-comparison.md` | 신규 (비교 결과) |
| `schedule/CLAUDE.md` | 신규 |
| `classsession/CLAUDE.md` | 신규 |
| `auth/CLAUDE.md` | 신규 |
| `cache/CLAUDE.md` | 신규 |
| `scripts/check-db-state.sh` | 신규 |
| `scripts/analyze-logs.sh` | 신규 |

---

## 검증 방법

- `/context` 명령어로 before/after 컨텍스트 %를 직접 비교
- 동일 시나리오 5개 실행 후 토큰 수 비교
- 답변 품질은 정확도 기준 1-5점 주관 평가
- 기대 결과: 컨텍스트 사용량 20-40% 절감, 불필요한 파일 읽기 감소
