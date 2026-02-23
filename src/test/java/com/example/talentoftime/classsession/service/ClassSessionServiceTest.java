package com.example.talentoftime.classsession.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.talentoftime.classsession.dto.ClassSessionBulkCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionResponse;
import com.example.talentoftime.classsession.fixture.ClassSessionFixture;
import com.example.talentoftime.classroom.fixture.ClassroomFixture;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.period.fixture.PeriodFixture;
import com.example.talentoftime.support.TestContainerSpringBootTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@TestContainerSpringBootTest
@Transactional
@DisplayName("수업 일정 서비스 테스트")
class ClassSessionServiceTest {

    @Autowired
    private ClassSessionService classSessionService;

    @Autowired
    private ClassSessionFixture classSessionFixture;

    @Autowired
    private PeriodFixture periodFixture;

    @Autowired
    private ClassroomFixture classroomFixture;

    @Nested
    @DisplayName("수업 일정 단건 조회")
    class 수업_일정을_단건_조회한다 {

        @Test
        void 수업_일정을_ID로_조회한다() {
            // Given
            var period = periodFixture.일교시();
            var classroom = classroomFixture.육백삼호();
            var session = classSessionFixture.수업일정(LocalDate.of(2026, 2, 24), period, classroom);

            // When
            ClassSessionResponse response = classSessionService.findClassSession(session.getId());

            // Then
            assertAll(
                    () -> assertThat(response.getId()).isEqualTo(session.getId()),
                    () -> assertThat(response.getDate()).isEqualTo(LocalDate.of(2026, 2, 24)),
                    () -> assertThat(response.getPeriodNumber()).isEqualTo(1),
                    () -> assertThat(response.getRoomNumber()).isEqualTo(603)
            );
        }

        @Test
        void 존재하지_않는_수업_일정을_조회하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> classSessionService.findClassSession(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CLASS_SESSION_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("날짜별 수업 일정 조회")
    class 날짜별_수업_일정을_조회한다 {

        @Test
        void 해당_날짜에_수업_일정이_없으면_빈_목록을_반환한다() {
            // When
            List<ClassSessionResponse> response =
                    classSessionService.findClassSessionsByDate(LocalDate.of(2026, 2, 24));

            // Then
            assertThat(response).isEmpty();
        }

        @Test
        void 날짜로_수업_일정_목록을_조회한다() {
            // Given
            LocalDate date = LocalDate.of(2026, 2, 24);
            classSessionFixture.수업일정(date, periodFixture.일교시(), classroomFixture.육백삼호());
            classSessionFixture.수업일정(date, periodFixture.이교시(), classroomFixture.육백사호());

            // When
            List<ClassSessionResponse> response = classSessionService.findClassSessionsByDate(date);

            // Then
            assertAll(
                    () -> assertThat(response).hasSize(2),
                    () -> assertThat(response).extracting("date").containsOnly(date)
            );
        }
    }

    @Nested
    @DisplayName("주간 수업 일정 조회")
    class 주간_수업_일정을_조회한다 {

        // 2026-02-22(일)에 명세를 받아 다음 주 월~금(23~27) 일정을 등록하는 시나리오
        private static final LocalDate SUNDAY = LocalDate.of(2026, 2, 22);
        private static final LocalDate MONDAY = LocalDate.of(2026, 2, 23);
        private static final LocalDate TUESDAY = LocalDate.of(2026, 2, 24);
        private static final LocalDate WEDNESDAY = LocalDate.of(2026, 2, 25);
        private static final LocalDate THURSDAY = LocalDate.of(2026, 2, 26);
        private static final LocalDate FRIDAY = LocalDate.of(2026, 2, 27);

        @Test
        void 주간_수업_일정이_없으면_빈_목록을_반환한다() {
            // When
            List<ClassSessionResponse> response = classSessionService.findWeeklyClassSessions(MONDAY);

            // Then
            assertThat(response).isEmpty();
        }

        @Test
        void 일요일_기준으로_다음주_월금_수업_일정을_일괄_등록하고_주간_조회한다() {
            // Given: 일요일(2026-02-22)에 다음 주 월~금 일정 명세를 일괄 등록
            var period1 = periodFixture.일교시();
            var period2 = periodFixture.이교시();
            var classroom603 = classroomFixture.육백삼호();
            var classroom604 = classroomFixture.육백사호();

            List<ClassSessionCreateRequest> sessions = List.of(
                    classSessionFixture.단건_요청(MONDAY,    period1.getPeriodNumber(), classroom603.getId()),
                    classSessionFixture.단건_요청(MONDAY,    period2.getPeriodNumber(), classroom604.getId()),
                    classSessionFixture.단건_요청(TUESDAY,   period1.getPeriodNumber(), classroom603.getId()),
                    classSessionFixture.단건_요청(WEDNESDAY, period1.getPeriodNumber(), classroom603.getId()),
                    classSessionFixture.단건_요청(THURSDAY,  period1.getPeriodNumber(), classroom603.getId()),
                    classSessionFixture.단건_요청(FRIDAY,    period1.getPeriodNumber(), classroom603.getId())
            );
            ClassSessionBulkCreateRequest bulkRequest = classSessionFixture.일괄_요청(sessions);
            classSessionService.createBulkClassSessions(bulkRequest);

            // When: 해당 주 임의 날짜로 주간 조회 (월~일 범위)
            List<ClassSessionResponse> response = classSessionService.findWeeklyClassSessions(MONDAY);

            // Then: 등록한 6개 일정이 모두 조회되어야 한다
            assertAll(
                    () -> assertThat(response).hasSize(6),
                    () -> assertThat(response).extracting("date")
                            .containsExactlyInAnyOrder(
                                    MONDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY),
                    () -> assertThat(response).extracting("date")
                            .doesNotContain(SUNDAY)
            );
        }

        @Test
        void 주간_조회는_해당_주_월요일부터_일요일까지만_반환한다() {
            // Given: 이번 주(월~금) + 다음 주(월) 일정 등록
            var period = periodFixture.일교시();
            var classroom = classroomFixture.육백삼호();

            classSessionFixture.수업일정(MONDAY, period, classroom);
            // 다음 주 월요일 (범위 밖)
            LocalDate nextMonday = MONDAY.plusWeeks(1);
            classSessionFixture.수업일정(nextMonday, periodFixture.이교시(), classroomFixture.육백사호());

            // When: 이번 주 기준 주간 조회
            List<ClassSessionResponse> response = classSessionService.findWeeklyClassSessions(MONDAY);

            // Then: 이번 주 데이터만 반환
            assertAll(
                    () -> assertThat(response).hasSize(1),
                    () -> assertThat(response.get(0).getDate()).isEqualTo(MONDAY)
            );
        }
    }

    @Nested
    @DisplayName("수업 일정 단건 등록")
    class 수업_일정을_단건_등록한다 {

        @Test
        void 수업_일정을_등록하면_응답을_반환한다() {
            // Given
            var period = periodFixture.일교시();
            var classroom = classroomFixture.육백삼호();
            ClassSessionCreateRequest request =
                    classSessionFixture.단건_요청(LocalDate.of(2026, 2, 24), period.getPeriodNumber(), classroom.getId());

            // When
            ClassSessionResponse response = classSessionService.createClassSession(request);

            // Then
            assertAll(
                    () -> assertThat(response.getId()).isNotNull(),
                    () -> assertThat(response.getDate()).isEqualTo(LocalDate.of(2026, 2, 24)),
                    () -> assertThat(response.getPeriodNumber()).isEqualTo(period.getPeriodNumber()),
                    () -> assertThat(response.getRoomNumber()).isEqualTo(classroom.getRoomNumber())
            );
        }

        @Test
        void 존재하지_않는_교시로_등록하면_예외가_발생한다() {
            // Given
            var classroom = classroomFixture.육백삼호();
            ClassSessionCreateRequest request =
                    classSessionFixture.단건_요청(LocalDate.of(2026, 2, 24), 99, classroom.getId());

            // When & Then
            assertThatThrownBy(() -> classSessionService.createClassSession(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.PERIOD_NOT_FOUND.getMessage());
        }

        @Test
        void 존재하지_않는_강의실로_등록하면_예외가_발생한다() {
            // Given
            periodFixture.일교시();
            ClassSessionCreateRequest request =
                    classSessionFixture.단건_요청(LocalDate.of(2026, 2, 24), 1, 999L);

            // When & Then
            assertThatThrownBy(() -> classSessionService.createClassSession(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CLASSROOM_NOT_FOUND.getMessage());
        }

        @Test
        void 같은_날짜_교시_강의실에_중복_등록하면_예외가_발생한다() {
            // Given
            var period = periodFixture.일교시();
            var classroom = classroomFixture.육백삼호();
            LocalDate date = LocalDate.of(2026, 2, 24);
            classSessionFixture.수업일정(date, period, classroom);

            ClassSessionCreateRequest request =
                    classSessionFixture.단건_요청(date, period.getPeriodNumber(), classroom.getId());

            // When & Then
            assertThatThrownBy(() -> classSessionService.createClassSession(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CLASS_SESSION_DUPLICATED.getMessage());
        }
    }

    @Nested
    @DisplayName("수업 일정 일괄 등록")
    class 수업_일정을_일괄_등록한다 {

        @Test
        void 일주일치_수업_일정을_일괄_등록한다() {
            // Given: 일요일(2026-02-22)에 다음 주 월~금 일정 명세 준비
            var period1 = periodFixture.일교시();
            var period2 = periodFixture.이교시();
            var classroom603 = classroomFixture.육백삼호();
            var classroom604 = classroomFixture.육백사호();

            LocalDate monday    = LocalDate.of(2026, 2, 23);
            LocalDate tuesday   = LocalDate.of(2026, 2, 24);
            LocalDate wednesday = LocalDate.of(2026, 2, 25);
            LocalDate thursday  = LocalDate.of(2026, 2, 26);
            LocalDate friday    = LocalDate.of(2026, 2, 27);

            List<ClassSessionCreateRequest> sessions = List.of(
                    classSessionFixture.단건_요청(monday,    period1.getPeriodNumber(), classroom603.getId()),
                    classSessionFixture.단건_요청(monday,    period2.getPeriodNumber(), classroom604.getId()),
                    classSessionFixture.단건_요청(tuesday,   period1.getPeriodNumber(), classroom603.getId()),
                    classSessionFixture.단건_요청(wednesday, period1.getPeriodNumber(), classroom603.getId()),
                    classSessionFixture.단건_요청(thursday,  period1.getPeriodNumber(), classroom603.getId()),
                    classSessionFixture.단건_요청(friday,    period1.getPeriodNumber(), classroom603.getId())
            );
            ClassSessionBulkCreateRequest request = classSessionFixture.일괄_요청(sessions);

            // When
            List<ClassSessionResponse> response = classSessionService.createBulkClassSessions(request);

            // Then
            assertAll(
                    () -> assertThat(response).hasSize(6),
                    () -> assertThat(response).extracting("date")
                            .containsExactlyInAnyOrder(monday, monday, tuesday, wednesday, thursday, friday),
                    () -> assertThat(response).extracting("periodNumber")
                            .containsExactlyInAnyOrder(
                                    period1.getPeriodNumber(), period2.getPeriodNumber(),
                                    period1.getPeriodNumber(), period1.getPeriodNumber(),
                                    period1.getPeriodNumber(), period1.getPeriodNumber())
            );
        }

        @Test
        void 일괄_등록_중_중복이_있으면_예외가_발생한다() {
            // Given
            var period = periodFixture.일교시();
            var classroom = classroomFixture.육백삼호();
            LocalDate date = LocalDate.of(2026, 2, 24);
            classSessionFixture.수업일정(date, period, classroom);

            List<ClassSessionCreateRequest> sessions = List.of(
                    classSessionFixture.단건_요청(date, period.getPeriodNumber(), classroom.getId())
            );
            ClassSessionBulkCreateRequest request = classSessionFixture.일괄_요청(sessions);

            // When & Then
            assertThatThrownBy(() -> classSessionService.createBulkClassSessions(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CLASS_SESSION_DUPLICATED.getMessage());
        }
    }
}
