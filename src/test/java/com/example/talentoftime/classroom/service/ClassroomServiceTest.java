package com.example.talentoftime.classroom.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classroom.dto.ClassroomCreateRequest;
import com.example.talentoftime.classroom.dto.ClassroomResponse;
import com.example.talentoftime.classroom.fixture.ClassroomFixture;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
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
@DisplayName("강의실 서비스 테스트")
class ClassroomServiceTest {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ClassroomFixture classroomFixture;

    @Nested
    @DisplayName("강의실 목록 조회")
    class 강의실_목록을_조회한다 {

        @Test
        void 강의실이_없으면_빈_목록을_반환한다() {
            // When
            List<ClassroomResponse> response = classroomService.findAllClassrooms();

            // Then
            assertThat(response).isEmpty();
        }

        @Test
        void 전체_강의실_목록을_반환한다() {
            // Given
            classroomFixture.육백삼호();
            classroomFixture.육백사호();

            // When
            List<ClassroomResponse> response = classroomService.findAllClassrooms();

            // Then
            assertThat(response).hasSize(2);
        }
    }

    @Nested
    @DisplayName("강의실 단건 조회")
    class 강의실을_단건_조회한다 {

        @Test
        void 강의실을_조회한다() {
            // Given
            Classroom classroom = classroomFixture.육백삼호();

            // When
            ClassroomResponse response = classroomService.findClassroom(classroom.getId());

            // Then
            assertThat(response.getRoomNumber()).isEqualTo(603);
        }

        @Test
        void 존재하지_않는_강의실을_조회하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> classroomService.findClassroom(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CLASSROOM_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("강의실 생성")
    class 강의실을_생성한다 {

        @Test
        void 강의실을_생성할_수_있다() {
            // Given
            ClassroomCreateRequest request = new ClassroomCreateRequest();
            ReflectionTestUtils.setField(request, "roomNumber", 603);

            // When
            ClassroomResponse response = classroomService.createClassroom(request);

            // Then
            assertAll(
                    () -> assertThat(response.getId()).isNotNull(),
                    () -> assertThat(response.getRoomNumber()).isEqualTo(603)
            );
        }

        @Test
        void 중복된_호수로_강의실을_생성하면_예외가_발생한다() {
            // Given
            classroomFixture.육백삼호();
            ClassroomCreateRequest request = new ClassroomCreateRequest();
            ReflectionTestUtils.setField(request, "roomNumber", 603);

            // When & Then
            assertThatThrownBy(() -> classroomService.createClassroom(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CLASSROOM_ROOM_NUMBER_DUPLICATED.getMessage());
        }
    }

    @Nested
    @DisplayName("강의실 삭제")
    class 강의실을_삭제한다 {

        @Test
        void 강의실을_삭제할_수_있다() {
            // Given
            Classroom classroom = classroomFixture.육백삼호();

            // When
            classroomService.deleteClassroom(classroom.getId());

            // Then
            assertThatThrownBy(() -> classroomService.findClassroom(classroom.getId()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CLASSROOM_NOT_FOUND.getMessage());
        }

        @Test
        void 존재하지_않는_강의실을_삭제하면_예외가_발생한다() {
            // When & Then
            assertThatThrownBy(() -> classroomService.deleteClassroom(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.CLASSROOM_NOT_FOUND.getMessage());
        }
    }
}
