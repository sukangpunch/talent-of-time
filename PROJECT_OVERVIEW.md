# talent-of-time 프로젝트 개요

시대인재 학원 알바생(크루)의 일일 업무 스케줄을 관리하는 백엔드 서버입니다.
크루별 작업 수행 횟수(count)를 기반으로 업무를 공평하게 분배하고,
강사별 세팅 정보와 스케줄 현황을 조교들이 실시간으로 확인할 수 있도록 합니다.

---

## 도메인 개념

| 도메인 | 설명 |
|--------|------|
| **Crew** | 알바생(조교). 인증 정보(username/password/email/role) 통합 |
| **CrewType** | `MORNING` / `MIDDLE` / `AFTERNOON` |
| **CrewRole** | `ADMIN` (리더급) / `USER` (일반 조교) |
| **Classroom** | 강의실 (603, 604, 605, 606호 등) |
| **Period** | 0교시(세팅) ~ 6교시. 교시별 시작/종료 시간 |
| **Teacher** | 강사 정보 및 강의실 세팅 방식 |
| **ClassSession** | 날짜 + 교시 + 강의실 + 강사 매핑 |
| **Schedule** | 날짜 × 교시 × 강의실 × 작업유형 × 크루 배정 결과 |
| **Count** | 크루별 작업유형별 누적 수행 횟수 |

---

## API 목록

### 1. 인증 API (`/api/auth`)

| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| `POST` | `/api/auth/login` | 로그인. JWT 토큰 발급 | 누구나 |
| `POST` | `/api/auth/logout` | 로그아웃 (클라이언트 토큰 폐기) | 누구나 |

---

### 2. 크루 API (`/api/crews`)

| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| `GET` | `/api/crews` | 전체 크루 조회. `?crewType=MORNING` 필터 가능 | ADMIN |
| `GET` | `/api/crews/{crewId}` | 특정 크루 단건 조회 | ADMIN |
| `POST` | `/api/crews` | 크루 등록 (이름, 유형, 인증 정보 입력) | ADMIN |
| `PATCH` | `/api/crews/{crewId}` | 크루 정보 수정 | ADMIN |
| `DELETE` | `/api/crews/{crewId}` | 크루 삭제 | ADMIN |

---

### 3. 강의실 API (`/api/classrooms`)

| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| `GET` | `/api/classrooms` | 전체 강의실 목록 조회 | ADMIN |
| `GET` | `/api/classrooms/{classroomId}` | 특정 강의실 단건 조회 | ADMIN |
| `POST` | `/api/classrooms` | 강의실 등록 (호수 입력) | ADMIN |
| `DELETE` | `/api/classrooms/{classroomId}` | 강의실 삭제 | ADMIN |

---

### 4. 교시 API (`/api/periods`)

| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| `GET` | `/api/periods` | 전체 교시 목록 조회 | ADMIN |
| `GET` | `/api/periods/{periodId}` | 특정 교시 단건 조회 | ADMIN |
| `POST` | `/api/periods` | 교시 등록 (교시번호, 시작/종료 시간) | ADMIN |
| `DELETE` | `/api/periods/{periodId}` | 교시 삭제 | ADMIN |

---

### 5. 강사 API (`/api/teachers`)

| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| `GET` | `/api/teachers` | 전체 강사 목록 조회 | 인증 필요 |
| `GET` | `/api/teachers/{teacherId}` | 강사 단건 조회 (세팅 정보 포함) | 인증 필요 |
| `POST` | `/api/teachers` | 강사 등록 | ADMIN |
| `PUT` | `/api/teachers/{teacherId}` | 강사 정보 수정 | ADMIN |
| `DELETE` | `/api/teachers/{teacherId}` | 강사 삭제 | ADMIN |

---

### 6. 수업 일정 API (`/api/class-sessions`)

| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| `GET` | `/api/class-sessions/{classSessionId}` | 단건 조회 (강사 세팅 정보 포함) | 인증 필요 |
| `GET` | `/api/class-sessions?date=YYYY-MM-DD` | 날짜별 수업 일정 조회 | 인증 필요 |
| `GET` | `/api/class-sessions/today` | 오늘 수업 일정 조회 | 인증 필요 |
| `GET` | `/api/class-sessions/weekly?date=YYYY-MM-DD` | 해당 날짜가 속한 주(월~일) 조회 | 인증 필요 |
| `POST` | `/api/class-sessions` | 수업 일정 단건 등록 (teacherId 선택) | ADMIN |
| `POST` | `/api/class-sessions/bulk` | 수업 일정 일괄 등록 | ADMIN |
| `PUT` | `/api/class-sessions/{classSessionId}` | 수업 일정 수정. **연결 스케줄 삭제 + count 복구** | ADMIN |
| `DELETE` | `/api/class-sessions/{classSessionId}` | 수업 일정 삭제. **연결 스케줄 삭제 + count 복구** | ADMIN |

---

### 7. 스케줄 API (`/api/schedules`)

| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| `GET` | `/api/schedules?date=YYYY-MM-DD` | 날짜별 전체 스케줄 조회 | 인증 필요 |
| `GET` | `/api/schedules?date=YYYY-MM-DD&periodNumber=1` | 날짜 + 교시 필터 조회 | 인증 필요 |
| `GET` | `/api/schedules/today` | 오늘 스케줄 조회 | 인증 필요 |
| `POST` | `/api/schedules` | **작업 자기 등록** (JWT 토큰 기반, count +1) | 인증 필요 |
| `DELETE` | `/api/schedules/{scheduleId}` | **작업 등록 취소** (본인 것만, count -1) | 인증 필요 |
| `POST` | `/api/schedules/daily-assign?date=YYYY-MM-DD` | 하루치 스케줄 자동 배정 | ADMIN |
| `POST` | `/api/schedules/setting?date=YYYY-MM-DD` | 세팅 스케줄 자동 배정 | ADMIN |
| `POST` | `/api/schedules/period` | 특정 교시+강의실 스케줄 배정 | ADMIN |
| `POST` | `/api/schedules/swap` | 두 스케줄의 담당 크루 교환 | ADMIN |

---

### 8. 카운트 API (`/api/counts`)

| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| `GET` | `/api/counts` | 전체 크루의 전체 작업 카운트 조회 | ADMIN |
| `GET` | `/api/counts/crew/{crewId}` | 특정 크루의 작업별 카운트 조회 | ADMIN |
| `GET` | `/api/counts/task-type/{taskType}` | 특정 작업 유형의 크루별 카운트 조회 | ADMIN |
| `DELETE` | `/api/counts/crew/{crewId}/reset` | 특정 크루의 카운트 전부 초기화 | ADMIN |
| `DELETE` | `/api/counts/reset` | 전체 카운트 초기화 | ADMIN |

---

### 9. 마이페이지 API (`/api/my`)

| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| `GET` | `/api/my/profile` | 내 정보 조회 (JWT 기반) | 인증 필요 |
| `GET` | `/api/my/counts` | 내 작업별 카운트 조회 | 인증 필요 |

---

## 인증/인가 정책

| 접근 범위 | 대상 |
|-----------|------|
| 인증 없이 접근 가능 | `/api/auth/**`, Swagger UI (`/swagger-ui/**`, `/v3/api-docs/**`) |
| 인증 필요 (USER 이상) | 스케줄 조회, 수업 일정 조회, 강사 조회, 마이페이지, 스케줄 자기 등록/취소 |
| ADMIN 전용 | 크루/강의실/교시/강사/수업 일정 관리, 자동 배정, count 관리 |

JWT 토큰: `Authorization: Bearer <token>` 헤더로 전달

---

## 스케줄 비즈니스 로직 상세

### 핵심 원칙

> 크루별 작업유형별 누적 count가 가장 낮은 크루에게 우선 배정한다.
> 동일한 시간대(교시)에는 동일 크루가 중복 배정되지 않도록 최소화한다.

### 교시별 배정 가능 크루 유형

| 교시 | 작업 | 배정 가능 크루 유형 |
|------|------|---------------------|
| 세팅(0교시) | SETTING | MORNING |
| 1교시 | ENTRY, JOG | MORNING |
| 1교시 | EXIT | MORNING, MIDDLE |
| 2교시 | ENTRY, JOG, EXIT | MORNING, MIDDLE |
| 3교시 | ENTRY, JOG | MORNING, MIDDLE |
| 3교시 | EXIT | AFTERNOON |
| 4교시 | ENTRY, JOG, EXIT | MIDDLE, AFTERNOON |
| 5, 6교시 | ENTRY, JOG, EXIT | AFTERNOON |

### 크루 선택 알고리즘

```
1. 해당 교시+작업에 배정 가능한 CrewType 목록 조회
2. 해당 CrewType에 속하는 모든 크루 조회
3. 각 크루의 해당 작업 count 조회
4. count가 가장 낮은 값(minCount) 산출
5. minCount를 보유한 크루들을 후보 목록으로 추출
6. 후보를 다음 우선순위로 정렬:
   ① 현재 교시에서 아직 배정되지 않은 크루 우선 (busyCrewIds에 없는 크루)
   ② 이름 오름차순 (동점 시 결정론적 선택)
7. 1순위 크루를 배정
8. 배정된 크루의 해당 작업 count +1
```

### 하루치 전체 자동 배정 (`POST /api/schedules/daily-assign`)

```
1. [검증] 해당 날짜의 ClassSession 존재 여부 확인 → 없으면 오류
2. [검증] 해당 날짜에 이미 스케줄 존재 여부 확인 → 있으면 오류
3. [필터] 1~3교시만 자동 배정 대상 (4~6교시 제외)
4. [세팅 배정] 수업이 있는 강의실 전체에 SETTING 작업 배정 (MORNING 크루)
5. [교시별 배정] 교시 번호 오름차순으로 순회
    ├─ 동일 교시 내 모든 강의실이 busyCrewIds 공유
    └─ 각 강의실마다 ENTRY → JOG → EXIT 순서로 배정
6. [저장] 세팅 스케줄 + 교시별 스케줄 일괄 저장
```

### 스케줄 교환 (`POST /api/schedules/swap`)

```
1. scheduleIdA, scheduleIdB 두 스케줄 조회
2. [검증] 두 스케줄의 TaskType이 같으면 오류 (의미 없는 교환)
3. count 보정:
   ├─ 크루A: taskTypeA count -1, taskTypeB count +1
   └─ 크루B: taskTypeB count -1, taskTypeA count +1
4. 두 스케줄의 crew 필드 맞교환
```

### 전체 데이터 흐름

```
[준비] 크루 등록 → 강의실 등록 → 교시 등록 → 강사 등록

[매주 일요일] 다음 주 수업 일정 등록 (ClassSession + Teacher 연결)

[매일]
  스케줄 자동 배정 (daily-assign) 또는 조교 자기 등록
      ↓
  스케줄 표 조회 (GET /api/schedules/today)
      ↓
  수업 일정 클릭 → ClassSession 상세 조회 (강사 세팅 정보 확인)
      ↓
  필요 시 수동 교환 (swap) 또는 등록 취소
```
