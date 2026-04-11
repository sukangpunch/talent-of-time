# 시대인재 크루 서비스

> 학원 알바생(크루)의 일일 업무 스케줄을 등록하고, 필수적인 정보 및 알림을 제공하는 서비스 

---

## 목차

- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [서버 아키텍처](#서버-아키텍처)
- [핵심 기능](#핵심-기능)
- [도메인 모델](#도메인-모델)
- [스케줄링 알고리즘](#스케줄링-알고리즘)
- [CI/CD 파이프라인](#cicd-파이프라인)
- [모니터링](#모니터링)
- [로컬 개발 환경](#로컬-개발-환경)

---

## 프로젝트 개요

시대인재 학원의 조교(크루)들이 매일 수행하는 **입실·조그·퇴실·세팅·시험** 작업을 자동으로 배정하는 시스템입니다.

- 크루별 작업 수행 횟수(Count)를 추적하여 특정 크루에게 업무가 몰리지 않도록 **공평하게 분배**합니다.
- 크루 유형(오전/미들/오후)별 출퇴근 시간을 고려한 **배정 풀(Pool) 제약**을 적용합니다.
- 강사별 세팅 정보(분필·마이크·PPT 등)를 앱에서 실시간으로 조회할 수 있습니다.

---

## 기술 스택

### Backend

| 분류 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Build Tool | Gradle |
| ORM | Spring Data JPA (Hibernate) |
| DB Migration | Flyway |
| Security | Spring Security, JWT, Kakao OAuth 2.0 |
| Cache | Redis (Spring Data Redis) |
| Async HTTP | Spring WebFlux (WebClient) |
| Push Notification | Firebase Cloud Messaging (FCM) |
| AI | Google Gemini API |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Test | JUnit 5, Testcontainers |

### Infrastructure

| 분류 | 기술 |
|------|------|
| Database | MySQL |
| Cache Store | Redis |
| Container | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| Image Registry | Docker Hub |
| Monitoring | Prometheus, Grafana |
| Log Aggregation | Grafana Loki, Grafana Alloy |

---

## 서버 아키텍처

### 전체 구성도

```
┌──────────────────────────────────────────────────────────┐
│                      Client (App)                        │
└────────────────────────┬─────────────────────────────────┘
                         │ HTTPS
              ┌──────────▼──────────┐
              │    Load Balancer    │
              └──────┬──────────────┘
          ┌──────────▼──────┐  ┌──────────────────┐
          │  Spring Server 1 │  │  Spring Server 2  │
          │  (ARM64 Docker)  │  │  (ARM64 Docker)   │
          └──────┬────────────┘  └───────┬──────────┘
                 │                       │
         ┌───────▼───────────────────────▼──────┐
         │              MySQL (RDS)              │
         └───────────────────────────────────────┘
                 │
         ┌───────▼───────────────────────────────┐
         │              Redis                    │
         │  (Session Token, Cache)               │
         └───────────────────────────────────────┘

[Monitoring Stack - 별도 서버]
  Grafana Alloy → Prometheus (Metrics)
  Grafana Alloy → Grafana Loki (Logs)
  Grafana (Dashboard)
```

### 애플리케이션 계층 구조

```
Controller
    ↓  (Request DTO)
Service
    ↓  (Domain / Repository)
Repository  ←→  MySQL
    ↑
Redis Cache (AOP 기반)
```

- **Controller**: HTTP 요청 처리, 입력값 검증(`@Valid`), 응답 포맷팅
- **Service**: 비즈니스 로직, 트랜잭션 관리, DTO 변환
- **Repository**: Spring Data JPA 기반 데이터 접근
- **Domain (Entity)**: JPA 엔티티, 핵심 도메인 규칙 포함

---

## 핵심 기능

### 1. 크루 스케줄 자동 배정 (현재 제외된 기능입니다)

- 교시(0~6교시) × 작업 유형(입실·조그·퇴실·세팅·시험) 조합별로 배정 가능한 크루 풀을 결정
- 풀 내에서 **누적 count가 가장 낮은 크루를 우선 배정**
- 동일 count 시 이름 오름차순(가나다순) 정렬로 결정론적 배정

### 2. 인증 (Kakao OAuth 2.0 + JWT)

- Kakao 소셜 로그인으로 크루 인증
- Access Token (단기) + Refresh Token (Redis 저장) 이중 토큰 전략
- Spring Security 기반 역할(ADMIN/USER) 접근 제어

### 3. Redis 캐싱 (AOP 기반)

- `@DefaultCaching`, `@DefaultCacheOut` 커스텀 어노테이션으로 선언적 캐시 적용
- AOP를 통해 서비스 계층 캐싱 로직 분리

### 4. 강사별 세팅 정보 관리

- 분필 종류, 수량, 마이크 종류, PPT 여부 등 강사별 세팅 정보 저장 및 조회
- 조교들이 강의 전 세팅 준비 시 즉시 참조 가능

### 5. 수업 일정 관리 (ClassSession)

- 날짜 + 교시 + 강의실 + 강사 조합으로 주간 수업 일정 등록
- 일정 수정/삭제 시 연결된 스케줄 자동 삭제 + Count 복구

### 6. Count 관리 (현재 제외된 기능입니다)

- 크루별 작업 유형별 수행 횟수 추적
- 작업 자기 등록/취소, 교환 시 Count 자동 조정

### 7. FCM 푸시 알림

- Firebase Cloud Messaging을 이용한 크루 대상 푸시 알림

### 8. API 문서

- Swagger UI (`/swagger-ui/index.html`)를 통한 REST API 자동 문서화

---

## 도메인 모델

```
Crew (크루/조교)
 ├── CrewType: MORNING | MIDDLE | AFTERNOON
 └── Role: ADMIN | USER

Period (교시)
 └── 0교시(세팅) ~ 6교시, 각 교시별 시작·종료 시간

Classroom (강의실)
 └── 603, 604, 605, 606호 등

Teacher (강사)
 ├── ChalkType: SCHOOL | PERSONAL | BOTH
 └── MicType: SCHOOL | PERSONAL

ClassSession (수업 세션)
 └── 날짜 × 교시 × 강의실 × 강사

TaskType (작업 유형)
 └── SETTING | ENTRY | JOG | EXIT | EXAM
```

---

## CI/CD 파이프라인

```
[GitHub Push → main]
        │
        ▼
┌───────────────┐
│   CI (Build)  │  Gradle 빌드 + 테스트
│               │  JUnit Report 자동 게시
└───────┬───────┘
        │ (CI 성공 시)
        ▼
┌───────────────────────┐
│   CD (Build & Push)   │  JAR 빌드
│                       │  Docker 이미지 빌드 (linux/arm64)
│                       │  Docker Hub Push
│                       │  구 이미지 자동 정리 (최신 2개 유지)
└──────────┬────────────┘
           │
     ┌─────┴─────┐
     ▼           ▼
 Server 1     Server 2
 SSH 접속     SSH 접속
 compose up   compose up
```

- **멀티 서버 병렬 배포**: `deploy`, `deploy-2` Job이 동시 실행
- **ARM64 빌드**: Oracle Cloud ARM 인스턴스 대응 (`linux/arm64`)
- **이미지 자동 정리**: Docker Hub 및 서버 로컬 이미지 최신 2개만 유지

---

## 모니터링

```
Spring Boot Actuator
       │
       │ /actuator/prometheus
       ▼
 Grafana Alloy  ──────────────────────┐
       │                              │
       │ metrics scrape               │ log tail (/var/log/spring)
       ▼                              ▼
  Prometheus                     Grafana Loki
       │                              │
       └───────────────┬──────────────┘
                       ▼
                   Grafana Dashboard
```

- **Prometheus**: Spring Boot Actuator의 `/actuator/prometheus` 엔드포인트 수집
- **Grafana Alloy**: 각 서버에서 메트릭·로그를 수집해 중앙 서버로 전송
- **Grafana Loki**: 애플리케이션 로그 수집 및 검색
- **Grafana**: 통합 대시보드로 서버 상태 모니터링

---

## 로컬 개발 환경

### 사전 요구사항

- Java 21
- Docker & Docker Compose
- MySQL (또는 로컬 Docker)
- Redis (또는 로컬 Docker)

### 빌드 및 실행

```bash
# 전체 빌드
./gradlew build

# 테스트 실행
./gradlew test

# 특정 테스트만 실행
./gradlew test --tests "com.example.talentoftime.SomeTestClass"

# 애플리케이션 실행
./gradlew bootRun

# 빌드 산출물 정리
./gradlew clean
```

> Windows 환경에서는 `gradlew.bat`을 사용합니다.

### 환경 변수

| 변수명 | 설명 |
|--------|------|
| `DB_URL` | MySQL JDBC URL |
| `DB_USERNAME` | DB 사용자명 |
| `DB_PASSWORD` | DB 비밀번호 |
| `JWT_SECRET` | JWT 서명 시크릿 |
| `KAKAO_CLIENT_ID` | Kakao OAuth Client ID |
| `KAKAO_CLIENT_SECRET` | Kakao OAuth Client Secret |
| `KAKAO_REDIRECT_URI` | Kakao OAuth Redirect URI |
| `REDIS_SERVER_HOST` | Redis 호스트 |
| `REDIS_PORT` | Redis 포트 |
| `REDIS_PASSWORD` | Redis 비밀번호 |
| `GEMINI_API_KEY` | Google Gemini API Key |
