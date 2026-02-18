---
name: test
description: 테스트 코드를 작성하거나 수정할 때 이 프로젝트의 테스트 컨벤션과 패턴을 참고합니다
---

# 테스트 코드 작성 가이드

## 테스트 기본 설정

모든 통합 테스트는 `@SpringBootTest` + `@Transactional` 어노테이션을 사용합니다.

```java
@SpringBootTest
@Transactional
@DisplayName("크루 서비스 테스트")
class CrewServiceTest {
    // 테스트 코드
}
```

**제공 기능:**
- MySQL 연결 (application.yml 기반)
- Spring Boot 컨텍스트 로드
- `@Transactional`로 테스트 후 자동 롤백
- JUnit 5 기반

## Fixture 패턴

테스트 데이터는 각 도메인 엔티티의 정적 팩토리 메서드(`create()`)를 활용하여 생성합니다.

**위치:** `src/test/java/com/example/talentoftime/[domain]/fixture/`

```
fixture/
└── [Entity]Fixture.java    # 편의 메서드 제공
```

### 예제: CrewFixture

```java
@TestComponent
@RequiredArgsConstructor
public class CrewFixture {

    private final CrewRepository crewRepository;
    private final CountRepository countRepository;

    // 편의 메서드: 기본값으로 생성
    public Crew 크루(String name, CrewType crewType) {
        Crew crew = Crew.create(name, crewType);
        crewRepository.save(crew);

        Arrays.stream(TaskType.values())
                .map(taskType -> Count.createInitial(crew, taskType))
                .forEach(countRepository::save);

        return crew;
    }

    public Crew 오전_크루() {
        return 크루("홍길동", CrewType.MORNING);
    }

    public Crew 오후_크루() {
        return 크루("김철수", CrewType.AFTERNOON);
    }
}
```

**편의 메서드 작성 팁:**

- 한국어 메서드명 사용 (가독성)
- 자주 사용되는 기본값 조합만 제공
- 엔티티의 `create()` 정적 팩토리 메서드를 조합하여 데이터 설정

### 테스트에서 사용

```java
@SpringBootTest
@Transactional
class CrewServiceTest {

    @Autowired
    private CrewFixture crewFixture;

    @Test
    void 크루_목록을_조회할_수_있다() {
        // 편의 메서드 사용
        Crew crew = crewFixture.오전_크루();

        // 직접 생성
        Crew customCrew = crewFixture.크루("박지성", CrewType.MIDDLE);
    }
}
```

## 테스트 네이밍 컨벤션

### 테스트 메서드 네이밍 규칙

테스트 메서드명은 **한국어로 명확하게** 작성하며, 다음 패턴을 따릅니다:

#### 1. 정상 동작 테스트

```java
// 패턴: 어떤_것을_하면_어떤_결과가_나온다
@Test
void 크루가_없으면_빈_목록을_반환한다() { ... }

@Test
void 크루_타입으로_필터링하여_조회한다() { ... }

@Test
void 크루를_생성하면_count가_초기화된다() { ... }

@Test
void 크루_이름과_타입을_수정할_수_있다() { ... }
```

#### 2. 예외 테스트

```java
// 패턴: 어떤_것을_하면_예외_응답을_반환한다
@Test
void 존재하지_않는_크루를_조회하면_예외가_발생한다() { ... }

@Test
void 중복된_이름으로_크루를_생성하면_예외가_발생한다() { ... }

@Test
void 배정_가능한_크루가_없으면_예외가_발생한다() { ... }

@Test
void 이미_배정된_스케줄에_재배정하면_예외가_발생한다() { ... }
```

## BDD 테스트 작성

테스트는 Given-When-Then 구조로 작성합니다.

```java
@Test
@DisplayName("크루 타입으로 필터링하여 조회한다")
void 크루_타입으로_필터링하여_조회한다() {
    // Given: 테스트 사전 조건
    Crew morning1 = crewFixture.크루("홍길동", CrewType.MORNING);
    Crew morning2 = crewFixture.크루("이순신", CrewType.MORNING);
    Crew afternoon = crewFixture.크루("김유신", CrewType.AFTERNOON);

    // When: 실제 동작
    List<CrewResponse> response = crewService.findCrewsByType(CrewType.MORNING);

    // Then: 결과 검증
    assertAll(
        () -> assertThat(response).hasSize(2),
        () -> assertThat(response).extracting("name")
                .containsExactlyInAnyOrder("홍길동", "이순신")
    );
}
```

## 테스트 그룹화 (@Nested)

기능별로 테스트를 그룹화합니다.

```java
@SpringBootTest
@Transactional
class CrewServiceTest {

    @Nested
    @DisplayName("크루 목록 조회")
    class 크루_목록을_조회한다 {

        @Test
        void 빈_목록을_반환한다() { ... }

        @Test
        void 크루_타입으로_필터링하여_조회한다() { ... }
    }

    @Nested
    @DisplayName("크루 생성")
    class 크루를_생성한다 {

        @BeforeEach
        void setUp() {
            // 이 그룹에만 적용되는 초기 설정
        }

        @Test
        void 크루를_생성하면_count가_초기화된다() { ... }

        @Test
        void 중복된_이름으로_크루를_생성하면_예외가_발생한다() { ... }
    }
}
```

## 자주 사용하는 Assertion

```java
// 기본 검증
assertThat(value).isEqualTo(expected);
assertThat(value).isNotNull();

// 컬렉션
assertThat(list).hasSize(3);
assertThat(list).isEmpty();
assertThat(list).extracting("name").containsExactlyInAnyOrder("홍길동", "이순신");

// 예외 검증 (BusinessException + ErrorCode)
assertThatThrownBy(() -> crewService.findCrew(999L))
    .isInstanceOf(BusinessException.class)
    .hasMessage(ErrorCode.CREW_NOT_FOUND.getMessage());

// 복수 검증
assertAll(
    () -> assertThat(response.getName()).isEqualTo("홍길동"),
    () -> assertThat(response.getCrewType()).isEqualTo(CrewType.MORNING)
);
```
