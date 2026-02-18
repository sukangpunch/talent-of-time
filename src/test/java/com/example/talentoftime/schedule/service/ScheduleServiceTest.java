package com.example.talentoftime.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classroom.fixture.ClassroomFixture;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.fixture.CrewFixture;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.period.fixture.PeriodFixture;
import com.example.talentoftime.schedule.domain.Schedule;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.dto.ScheduleSwapRequest;
import com.example.talentoftime.schedule.repository.ScheduleRepository;
import java.time.LocalDate;
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
@DisplayName("스케줄 서비스 테스트")
class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CrewFixture crewFixture;

    @Autowired
    private ClassroomFixture classroomFixture;

    @Autowired
    private PeriodFixture periodFixture;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Nested
    @DisplayName("날짜별 스케줄 조회")
    class 날짜별_스케줄을_조회한다 {

        @Test
        void 스케줄이_없으면_빈_목록을_반환한다() {
            // When
            List<ScheduleResponse> response = scheduleService.findSchedulesByDate(LocalDate.of(2026, 2, 18));

            // Then
            assertThat(response).isEmpty();
        }

        @Test
        void 날짜로_스케줄_목록을_조회한다() {
            // Given
            LocalDate date = LocalDate.of(2026, 2, 18);
            Crew crew = crewFixture.오전_크루();
            Classroom classroom = classroomFixture.육백삼호();
            Period period = periodFixture.일교시();
            scheduleRepository.save(Schedule.create(date, period, classroom, TaskType.ENTRY, crew));
            scheduleRepository.save(Schedule.create(date, period, classroom, TaskType.JOG, crew));

            // When
            List<ScheduleResponse> response = scheduleService.findSchedulesByDate(date);

            // Then
            assertThat(response).hasSize(2);
        }
    }

    @Nested
    @DisplayName("날짜 + 교시별 스케줄 조회")
    class 날짜와_교시로_스케줄을_조회한다 {

        @Test
        void 날짜와_교시_번호로_스케줄_목록을_조회한다() {
            // Given
            LocalDate date = LocalDate.of(2026, 2, 18);
            Crew crew = crewFixture.오전_크루();
            Classroom classroom = classroomFixture.육백삼호();
            Period period = periodFixture.일교시();
            scheduleRepository.save(Schedule.create(date, period, classroom, TaskType.ENTRY, crew));

            // When
            List<ScheduleResponse> response = scheduleService.findSchedulesByDateAndPeriod(date, 1);

            // Then
            assertAll(
                    () -> assertThat(response).hasSize(1),
                    () -> assertThat(response.get(0).getTaskType()).isEqualTo("ENTRY")
            );
        }

        @Test
        void 존재하지_않는_교시_번호로_조회하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> scheduleService.findSchedulesByDateAndPeriod(LocalDate.now(), 99))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.PERIOD_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("세팅 스케줄 자동 배정")
    class 세팅_스케줄을_자동_배정한다 {

        @Test
        void 강의실_수만큼_세팅_스케줄이_배정된다() {
            // Given
            LocalDate date = LocalDate.of(2026, 2, 18);
            periodFixture.세팅_교시();
            classroomFixture.육백삼호();
            classroomFixture.육백사호();
            crewFixture.오전_크루();

            // When
            List<ScheduleResponse> response = scheduleService.assignSettingSchedules(date);

            // Then
            assertAll(
                    () -> assertThat(response).hasSize(2),
                    () -> assertThat(response).extracting("taskType").containsOnly("SETTING"),
                    () -> assertThat(response).extracting("date").containsOnly(date)
            );
        }

        @Test
        void 세팅_교시가_없으면_예외가_발생한다() {
            // Given
            classroomFixture.육백삼호();
            crewFixture.오전_크루();

            // When & Then
            assertThatThrownBy(() -> scheduleService.assignSettingSchedules(LocalDate.now()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.PERIOD_NOT_FOUND.getMessage());
        }

        @Test
        void 배정_가능한_크루가_없으면_예외가_발생한다() {
            // Given: 오전 크루 없이 오후 크루만 존재 (세팅은 오전 크루만 가능)
            periodFixture.세팅_교시();
            classroomFixture.육백삼호();
            crewFixture.오후_크루();

            // When & Then
            assertThatThrownBy(() -> scheduleService.assignSettingSchedules(LocalDate.now()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.SCHEDULE_NO_ELIGIBLE_CREW.getMessage());
        }
    }

    @Nested
    @DisplayName("교시 스케줄 자동 배정")
    class 교시_스케줄을_자동_배정한다 {

        @Test
        void 교시_스케줄이_배정된다() {
            // Given
            LocalDate date = LocalDate.of(2026, 2, 18);
            Period period = periodFixture.일교시();
            Classroom classroom = classroomFixture.육백삼호();
            crewFixture.오전_크루();

            // When
            List<ScheduleResponse> response = scheduleService.assignPeriodSchedules(
                    date, period.getPeriodNumber(), classroom.getId());

            // Then: 1교시 = ENTRY + JOG + EXIT
            assertAll(
                    () -> assertThat(response).hasSize(3),
                    () -> assertThat(response).extracting("taskType")
                            .containsExactlyInAnyOrder("ENTRY", "JOG", "EXIT")
            );
        }

        @Test
        void 이미_배정된_스케줄에_재배정하면_예외가_발생한다() {
            // Given
            LocalDate date = LocalDate.of(2026, 2, 18);
            Period period = periodFixture.일교시();
            Classroom classroom = classroomFixture.육백삼호();
            crewFixture.오전_크루();
            scheduleService.assignPeriodSchedules(date, period.getPeriodNumber(), classroom.getId());

            // When & Then
            assertThatThrownBy(() -> scheduleService.assignPeriodSchedules(
                    date, period.getPeriodNumber(), classroom.getId()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.SCHEDULE_ALREADY_ASSIGNED.getMessage());
        }

        @Test
        void 존재하지_않는_교시로_배정하면_예외가_발생한다() {
            // Given
            Classroom classroom = classroomFixture.육백삼호();

            // When & Then
            assertThatThrownBy(() -> scheduleService.assignPeriodSchedules(
                    LocalDate.now(), 99, classroom.getId()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.PERIOD_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("스케줄 교환")
    class 스케줄을_교환한다 {

        @Test
        void 두_스케줄의_크루를_교환한다() {
            // Given
            LocalDate date = LocalDate.of(2026, 2, 18);
            Crew crewA = crewFixture.크루("홍길동", com.example.talentoftime.crew.domain.CrewType.MORNING);
            Crew crewB = crewFixture.크루("이순신", com.example.talentoftime.crew.domain.CrewType.MORNING);
            Period period = periodFixture.일교시();
            Classroom classroom = classroomFixture.육백삼호();

            Schedule scheduleA = Schedule.create(date, period, classroom, TaskType.ENTRY, crewA);
            Schedule scheduleB = Schedule.create(date, period, classroom, TaskType.JOG, crewB);
            scheduleRepository.save(scheduleA);
            scheduleRepository.save(scheduleB);

            ScheduleSwapRequest request = new ScheduleSwapRequest();
            ReflectionTestUtils.setField(request, "scheduleIdA", scheduleA.getId());
            ReflectionTestUtils.setField(request, "scheduleIdB", scheduleB.getId());

            // When
            scheduleService.swapSchedules(request);

            // Then
            Schedule swappedA = scheduleRepository.findById(scheduleA.getId()).get();
            Schedule swappedB = scheduleRepository.findById(scheduleB.getId()).get();
            assertAll(
                    () -> assertThat(swappedA.getCrew().getId()).isEqualTo(crewB.getId()),
                    () -> assertThat(swappedB.getCrew().getId()).isEqualTo(crewA.getId())
            );
        }

        @Test
        void 동일한_작업_유형의_스케줄을_교환하면_예외가_발생한다() {
            // Given
            LocalDate date = LocalDate.of(2026, 2, 18);
            Crew crewA = crewFixture.오전_크루();
            Crew crewB = crewFixture.미들_크루();
            Period period = periodFixture.일교시();
            Classroom classroom = classroomFixture.육백삼호();

            Schedule scheduleA = Schedule.create(date, period, classroom, TaskType.ENTRY, crewA);
            Schedule scheduleB = Schedule.create(date, period, classroom, TaskType.ENTRY, crewB);
            scheduleRepository.save(scheduleA);
            scheduleRepository.save(scheduleB);

            ScheduleSwapRequest request = new ScheduleSwapRequest();
            ReflectionTestUtils.setField(request, "scheduleIdA", scheduleA.getId());
            ReflectionTestUtils.setField(request, "scheduleIdB", scheduleB.getId());

            // When & Then
            assertThatThrownBy(() -> scheduleService.swapSchedules(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.SCHEDULE_SWAP_SAME_TASK.getMessage());
        }

        @Test
        void 존재하지_않는_스케줄을_교환하면_예외가_발생한다() {
            // Given
            ScheduleSwapRequest request = new ScheduleSwapRequest();
            ReflectionTestUtils.setField(request, "scheduleIdA", 999L);
            ReflectionTestUtils.setField(request, "scheduleIdB", 998L);

            // When & Then
            assertThatThrownBy(() -> scheduleService.swapSchedules(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.SCHEDULE_NOT_FOUND.getMessage());
        }
    }
}
