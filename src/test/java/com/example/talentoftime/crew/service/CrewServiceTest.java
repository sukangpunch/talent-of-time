package com.example.talentoftime.crew.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.crew.dto.CrewCreateRequest;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.CrewUpdateRequest;
import com.example.talentoftime.crew.fixture.CrewFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.example.talentoftime.support.TestContainerSpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@TestContainerSpringBootTest
@Transactional
@DisplayName("크루 서비스 테스트")
class CrewServiceTest {

    @Autowired
    private CrewService crewService;

    @Autowired
    private CrewFixture crewFixture;

    @Autowired
    private CountRepository countRepository;

    @Nested
    @DisplayName("크루 목록 조회")
    class 크루_목록을_조회한다 {

        @Test
        void 크루가_없으면_빈_목록을_반환한다() {
            // When
            List<CrewResponse> response = crewService.findAllCrews();

            // Then
            assertThat(response).isEmpty();
        }

        @Test
        void 전체_크루_목록을_반환한다() {
            // Given
            crewFixture.오전_크루();
            crewFixture.미들_크루();
            crewFixture.오후_크루();

            // When
            List<CrewResponse> response = crewService.findAllCrews();

            // Then
            assertThat(response).hasSize(3);
        }

        @Test
        void 크루_타입으로_필터링하여_조회한다() {
            // Given
            crewFixture.크루("홍길동", CrewType.MORNING);
            crewFixture.크루("이순신", CrewType.MORNING);
            crewFixture.오후_크루();

            // When
            List<CrewResponse> response = crewService.findCrewsByType(CrewType.MORNING);

            // Then
            assertAll(
                    () -> assertThat(response).hasSize(2),
                    () -> assertThat(response).extracting("name")
                            .containsExactlyInAnyOrder("홍길동", "이순신")
            );
        }
    }

    @Nested
    @DisplayName("크루 단건 조회")
    class 크루를_단건_조회한다 {

        @Test
        void 크루를_조회한다() {
            // Given
            Crew crew = crewFixture.오전_크루();

            // When
            CrewResponse response = crewService.findCrew(crew.getId());

            // Then
            assertAll(
                    () -> assertThat(response.getName()).isEqualTo(crew.getName()),
                    () -> assertThat(response.getCrewType()).isEqualTo(crew.getCrewType())
            );
        }

        @Test
        void 존재하지_않는_크루를_조회하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> crewService.findCrew(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CREW_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("크루 생성")
    class 크루를_생성한다 {

        @Test
        void 크루를_생성하면_count가_초기화된다() {
            // Given
            CrewCreateRequest request = new CrewCreateRequest();
            ReflectionTestUtils.setField(request, "name", "홍길동");
            ReflectionTestUtils.setField(request, "crewType", CrewType.MORNING);

            // When
            CrewResponse response = crewService.createCrew(request);

            // Then
            assertAll(
                    () -> assertThat(response.getName()).isEqualTo("홍길동"),
                    () -> assertThat(response.getCrewType()).isEqualTo(CrewType.MORNING)
            );
            assertThat(countRepository.findAll()).hasSize(TaskType.values().length);
        }

        @Test
        void 중복된_이름으로_크루를_생성하면_예외가_발생한다() {
            // Given
            crewFixture.크루("홍길동", CrewType.MORNING);
            CrewCreateRequest request = new CrewCreateRequest();
            ReflectionTestUtils.setField(request, "name", "홍길동");
            ReflectionTestUtils.setField(request, "crewType", CrewType.AFTERNOON);

            // When & Then
            assertThatThrownBy(() -> crewService.createCrew(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CREW_NAME_DUPLICATED.getMessage());
        }
    }

    @Nested
    @DisplayName("크루 수정")
    class 크루를_수정한다 {

        @Test
        void 크루_이름과_타입을_수정할_수_있다() {
            // Given
            Crew crew = crewFixture.오전_크루();
            CrewUpdateRequest request = new CrewUpdateRequest();
            ReflectionTestUtils.setField(request, "name", "박지성");
            ReflectionTestUtils.setField(request, "crewType", CrewType.AFTERNOON);

            // When
            CrewResponse response = crewService.updateCrew(crew.getId(), request);

            // Then
            assertAll(
                    () -> assertThat(response.getName()).isEqualTo("박지성"),
                    () -> assertThat(response.getCrewType()).isEqualTo(CrewType.AFTERNOON)
            );
        }

        @Test
        void 존재하지_않는_크루를_수정하면_예외가_발생한다() {
            // Given
            CrewUpdateRequest request = new CrewUpdateRequest();
            ReflectionTestUtils.setField(request, "name", "박지성");

            // When & Then
            assertThatThrownBy(() -> crewService.updateCrew(999L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CREW_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("크루 삭제")
    class 크루를_삭제한다 {

        @Test
        void 크루를_삭제할_수_있다() {
            // Given
            Crew crew = crewFixture.오전_크루();

            // When
            crewService.deleteCrew(crew.getId());

            // Then
            assertThatThrownBy(() -> crewService.findCrew(crew.getId()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CREW_NOT_FOUND.getMessage());
        }

        @Test
        void 존재하지_않는_크루를_삭제하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> crewService.deleteCrew(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CREW_NOT_FOUND.getMessage());
        }
    }
}
