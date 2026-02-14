# CLAUDE.md

이 파일은 Claude Code가 talent-of-time 저장소에서 작업할 때 참고하는 가이드입니다.

## 프로젝트 개요

시대인재 학원 알바생(크루)의 일일 업무 스케줄을 자동으로 배정하고 관리하는 백엔드 서버입니다.
크루별 작업 수행 횟수(count)를 기반으로 공평하게 업무를 분배합니다.

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.3.5
- **빌드 도구**: Gradle
- **데이터베이스**: MySQL
- **ORM**: Spring Data JPA

---

## 빌드 및 개발 명령어

```bash
# 전체 빌드
gradlew.bat build

# 테스트 실행
gradlew.bat test

# 특정 테스트만 실행
gradlew.bat test --tests "com.example.talentoftime.SomeTestClass"

# 애플리케이션 실행
gradlew.bat bootRun

# 빌드 산출물 정리
gradlew.bat clean
```

Windows 환경에서는 `gradlew.bat`, Unix 환경에서는 `./gradlew`를 사용합니다.

---

## 프로젝트 구조

```
talent-of-time/
├── src/
│   ├── main/
│   │   ├── java/com/example/talentoftime/
│   │   │   ├── TalentOfTimeApplication.java   # 엔트리 포인트
│   │   │   ├── [domain]/                       # 도메인별 폴더
│   │   │   │   ├── controller/                # REST API 엔드포인트
│   │   │   │   ├── service/                   # 비즈니스 로직
│   │   │   │   ├── domain/                    # JPA Entity
│   │   │   │   ├── repository/                # 데이터 접근 계층
│   │   │   │   └── dto/                       # DTO (Request/Response)
│   │   │   └── common/                        # 공통 기능
│   │   │       ├── exception/                 # 커스텀 예외
│   │   │       ├── config/                    # Spring 설정
│   │   │       └── util/                      # 유틸리티
│   │   └── resources/
│   │       └── application.yml                # 설정 파일
│   └── test/
│       └── java/com/example/talentoftime/
├── build.gradle                               # Gradle 빌드 설정
├── requirements.md                            # 전체 요구사항
└── requirement-server.md                      # 백엔드 서버 기능 요구사항
```

---

## 아키텍처

### 계층형 아키텍처 (Layered Architecture)

```
Controller → Service → Repository/Domain
```

**각 계층의 역할:**

- **Controller**: HTTP 요청 처리, 입력값 검증, 응답 포맷팅
- **Service**: 비즈니스 로직 처리, DTO 변환, 트랜잭션 관리
- **Repository**: 데이터 접근 계층, DB 쿼리 작성
- **Domain (Entity)**: JPA 엔티티, 도메인 모델

**주요 규칙:**

- 역계층 참조 금지 (예: Repository에서 Service 참조 불가)
- Service는 Repository를 주입받아 사용
- Controller는 Service를 주입받아 사용
- Entity는 도메인 로직만 포함
- DTO는 요청/응답 시에만 사용

---

## 도메인 모델

| 도메인 | 설명 |
|--------|------|
| 크루 (Crew) | 알바생 정보. 유형: 오전/미들/오후, 각 유형별 근무 시간대 |
| 강의실 (Classroom) | 강의실 정보 (603, 604, 605, 606호 등) |
| 교시 (Period) | 1~4교시, 각 교시별 시작/종료 시간 |
| 작업 유형 (TaskType) | 세팅/입실/조그/퇴실/시후 |
| 스케줄 (Schedule) | 날짜-교시-강의실-작업-크루 매핑 |
| 카운트 (Count) | 크루별 작업유형별 누적 수행 횟수 (공평 배정용) |

상세 도메인 정의는 `requirement-server.md` 참고.

---

## 개발 컨벤션

### 코드 스타일

- **클래스 선언 전 줄바꿈**: 클래스 정의 앞에 빈 줄 필수
- **파일 끝 줄바꿈**: 모든 파일은 개행 문자로 종료
- **와일드카드 import 금지**: 명시적 import만 사용
- **파라미터 줄바꿈**: Controller는 필수, 3개 이상의 파라미터가 있으면 줄바꿈
- **private 메서드 위치**: 호출하는 public 메서드 바로 아래 위치
- **원시 타입 사용**: null이 아닌 값은 `int`, `long` 등 원시 타입 사용, nullable은 Wrapper 사용
- **JPA @Column**: Entity의 모든 필드에 `@Column` 속성과 필드명 지정

### 네이밍 컨벤션

```java
// DTO 변환
// 다중 파라미터: of() 메서드
public static CrewDto of(Crew crew, CrewType type) { ... }

// 단일 파라미터: from() 메서드
public static CrewDto from(Crew crew) { ... }

// API 요청/응답
// XXXRequest, XXXResponse 형식
public class ScheduleCreateRequest { ... }
public class ScheduleCreateResponse { ... }

// REST API 엔드포인트
// kebab-case 사용
@GetMapping("/crew-schedule")      // O
@GetMapping("/crewSchedule")       // X
```

---

## 기술 스택 상세

### Core Framework

- **Spring Boot 3.3.5**: 스프링 부트
- **Spring Data JPA**: ORM (Hibernate)
- **Spring Web MVC**: REST 컨트롤러

### 데이터베이스

- **MySQL**: 주 데이터베이스
- **mysql-connector-j**: MySQL 드라이버 (runtime)

### 개발 도구

- **Lombok**: 보일러플레이트 코드 감소 (annotation processing)
- **JUnit 5**: 테스트 프레임워크

---

## Git 커밋 컨벤션

### 형식

```
<type>: <description>

[optional body]
```

### Type 목록

```
feat:     새로운 기능 추가
fix:      버그 수정
refactor: 코드 리팩토링 (기능 변경 없음)
docs:     문서 변경
test:     테스트 추가/수정
chore:    빌드 설정, 패키지 관리
perf:     성능 개선
```

### 예제

```bash
feat: 크루 스케줄 자동 배정 기능 추가
fix: count 기반 배정 시 동일 count 처리 버그 수정
refactor: ScheduleService 메서드 분리
test: ScheduleService 테스트 케이스 추가
```

---

## 주요 파일 위치

| 파일/폴더 | 설명 |
|----------|------|
| `src/main/java/com/example/talentoftime/` | 메인 소스 코드 |
| `src/test/java/com/example/talentoftime/` | 테스트 코드 |
| `src/main/resources/application.yml` | 설정 파일 |
| `build.gradle` | Gradle 빌드 설정 |
| `requirement-server.md` | 백엔드 서버 기능 요구사항 |
| `requirements.md` | 전체 요구사항 |

---

## 자주하는 작업

### 새 기능 추가

1. Entity 생성 (`src/main/java/.../domain/`)
2. Repository 작성 (`src/main/java/.../repository/`)
3. Service 구현 (`src/main/java/.../service/`)
4. Controller 작성 (`src/main/java/.../controller/`)
5. DTO 정의 (`src/main/java/.../dto/`)
6. 테스트 코드 작성

---

## 주의사항

1. **Java 21 toolchain** — Gradle이 필요 시 자동 다운로드
2. **Lombok** — annotation processor로 설정됨, IDE에서 Lombok 플러그인 필요
3. **테스트는 독립적** — 테스트 간 데이터 공유 불가
4. **한국어 메서드명** — 테스트 가독성 향상을 위해 사용
