package com.example.talentoftime.count.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.count.domain.Count;
import com.example.talentoftime.count.dto.CountResponse;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.fixture.CrewFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.example.talentoftime.support.TestContainerSpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@TestContainerSpringBootTest
@Transactional
@DisplayName("카운트 서비스 테스트")
class CountServiceTest {

    @Autowired
    private CountService countService;

    @Autowired
    private CrewFixture crewFixture;

    @Autowired
    private CountRepository countRepository;

    @Nested
    @DisplayName("전체 카운트 조회")
    class 전체_카운트를_조회한다 {

        @Test
        void 카운트가_없으면_빈_목록을_반환한다() {
            // When
            List<CountResponse> response = countService.findAllCounts();

            // Then
            assertThat(response).isEmpty();
        }

        @Test
        void 전체_카운트_목록을_반환한다() {
            // Given
            crewFixture.오전_크루();
            crewFixture.오후_크루();

            // When
            List<CountResponse> response = countService.findAllCounts();

            // Then: 크루 2명 × TaskType 5개 = 10개
            assertThat(response).hasSize(TaskType.values().length * 2);
        }
    }

    @Nested
    @DisplayName("크루별 카운트 조회")
    class 크루별_카운트를_조회한다 {

        @Test
        void 크루의_카운트_목록을_반환한다() {
            // Given
            Crew crew = crewFixture.오전_크루();

            // When
            List<CountResponse> response = countService.findCountsByCrew(crew.getId());

            // Then: TaskType 개수만큼 count 존재
            assertThat(response).hasSize(TaskType.values().length);
        }

        @Test
        void 크루의_카운트가_초기값_0으로_조회된다() {
            // Given
            Crew crew = crewFixture.오전_크루();

            // When
            List<CountResponse> response = countService.findCountsByCrew(crew.getId());

            // Then
            assertThat(response).extracting("count").containsOnly(0);
        }

        @Test
        void 존재하지_않는_크루로_조회하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> countService.findCountsByCrew(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CREW_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("작업 유형별 카운트 조회")
    class 작업_유형별_카운트를_조회한다 {

        @Test
        void 작업_유형으로_카운트_목록을_조회한다() {
            // Given
            crewFixture.오전_크루();
            crewFixture.오후_크루();

            // When
            List<CountResponse> response = countService.findCountsByTaskType(TaskType.ENTRY);

            // Then: 크루 2명에 대한 ENTRY count
            assertThat(response).hasSize(2);
        }

        @Test
        void 크루가_없으면_빈_목록을_반환한다() {
            // When
            List<CountResponse> response = countService.findCountsByTaskType(TaskType.SETTING);

            // Then
            assertThat(response).isEmpty();
        }
    }

    @Nested
    @DisplayName("크루별 카운트 초기화")
    class 크루별_카운트를_초기화한다 {

        @Test
        void 크루의_카운트를_초기화한다() {
            // Given
            Crew crew = crewFixture.오전_크루();
            Count entryCount = countRepository.findByCrewAndTaskType(crew, TaskType.ENTRY).get();
            entryCount.increment();
            entryCount.increment();

            // When
            countService.resetCountsByCrew(crew.getId());

            // Then
            Count resetCount = countRepository.findByCrewAndTaskType(crew, TaskType.ENTRY).get();
            assertThat(resetCount.getCount()).isZero();
        }

        @Test
        void 존재하지_않는_크루의_카운트를_초기화하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> countService.resetCountsByCrew(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CREW_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("전체 카운트 초기화")
    class 전체_카운트를_초기화한다 {

        @Test
        void 전체_카운트를_초기화한다() {
            // Given
            Crew crew = crewFixture.오전_크루();
            Count entryCount = countRepository.findByCrewAndTaskType(crew, TaskType.ENTRY).get();
            Count settingCount = countRepository.findByCrewAndTaskType(crew, TaskType.SETTING).get();
            entryCount.increment();
            settingCount.increment();
            settingCount.increment();

            // When
            countService.resetAllCounts();

            // Then
            List<CountResponse> response = countService.findAllCounts();
            assertThat(response).extracting("count").containsOnly(0);
        }
    }
}
