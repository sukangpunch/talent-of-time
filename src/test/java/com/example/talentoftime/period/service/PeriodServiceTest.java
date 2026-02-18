package com.example.talentoftime.period.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.period.dto.PeriodCreateRequest;
import com.example.talentoftime.period.dto.PeriodResponse;
import com.example.talentoftime.period.fixture.PeriodFixture;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@DisplayName("교시 서비스 테스트")
class PeriodServiceTest {

    @Autowired
    private PeriodService periodService;

    @Autowired
    private PeriodFixture periodFixture;

    @Nested
    @DisplayName("교시 목록 조회")
    class 교시_목록을_조회한다 {

        @Test
        void 교시가_없으면_빈_목록을_반환한다() {
            // When
            List<PeriodResponse> response = periodService.findAllPeriods();

            // Then
            assertThat(response).isEmpty();
        }

        @Test
        void 전체_교시_목록을_반환한다() {
            // Given
            periodFixture.세팅_교시();
            periodFixture.일교시();
            periodFixture.이교시();

            // When
            List<PeriodResponse> response = periodService.findAllPeriods();

            // Then
            assertThat(response).hasSize(3);
        }
    }

    @Nested
    @DisplayName("교시 단건 조회")
    class 교시를_단건_조회한다 {

        @Test
        void 교시를_조회한다() {
            // Given
            Period period = periodFixture.일교시();

            // When
            PeriodResponse response = periodService.findPeriod(period.getId());

            // Then
            assertAll(
                    () -> assertThat(response.getPeriodNumber()).isEqualTo(1),
                    () -> assertThat(response.getStartTime()).isEqualTo(LocalTime.of(9, 0)),
                    () -> assertThat(response.getEndTime()).isEqualTo(LocalTime.of(11, 30))
            );
        }

        @Test
        void 존재하지_않는_교시를_조회하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> periodService.findPeriod(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.PERIOD_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("교시 생성")
    class 교시를_생성한다 {

        @Test
        void 교시를_생성할_수_있다() {
            // Given
            PeriodCreateRequest request = new PeriodCreateRequest();
            ReflectionTestUtils.setField(request, "periodNumber", 1);
            ReflectionTestUtils.setField(request, "startTime", LocalTime.of(9, 0));
            ReflectionTestUtils.setField(request, "endTime", LocalTime.of(11, 30));

            // When
            PeriodResponse response = periodService.createPeriod(request);

            // Then
            assertAll(
                    () -> assertThat(response.getId()).isNotNull(),
                    () -> assertThat(response.getPeriodNumber()).isEqualTo(1),
                    () -> assertThat(response.getStartTime()).isEqualTo(LocalTime.of(9, 0)),
                    () -> assertThat(response.getEndTime()).isEqualTo(LocalTime.of(11, 30))
            );
        }

        @Test
        void 중복된_교시_번호로_생성하면_예외가_발생한다() {
            // Given
            periodFixture.일교시();
            PeriodCreateRequest request = new PeriodCreateRequest();
            ReflectionTestUtils.setField(request, "periodNumber", 1);
            ReflectionTestUtils.setField(request, "startTime", LocalTime.of(10, 0));
            ReflectionTestUtils.setField(request, "endTime", LocalTime.of(12, 0));

            // When & Then
            assertThatThrownBy(() -> periodService.createPeriod(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.PERIOD_NUMBER_DUPLICATED.getMessage());
        }
    }

    @Nested
    @DisplayName("교시 삭제")
    class 교시를_삭제한다 {

        @Test
        void 교시를_삭제할_수_있다() {
            // Given
            Period period = periodFixture.일교시();

            // When
            periodService.deletePeriod(period.getId());

            // Then
            assertThatThrownBy(() -> periodService.findPeriod(period.getId()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.PERIOD_NOT_FOUND.getMessage());
        }

        @Test
        void 존재하지_않는_교시를_삭제하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> periodService.deletePeriod(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.PERIOD_NOT_FOUND.getMessage());
        }
    }
}
