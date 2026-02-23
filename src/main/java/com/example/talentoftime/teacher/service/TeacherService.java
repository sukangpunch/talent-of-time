package com.example.talentoftime.teacher.service;

import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.teacher.domain.Teacher;
import com.example.talentoftime.teacher.dto.TeacherCreateRequest;
import com.example.talentoftime.teacher.dto.TeacherResponse;
import com.example.talentoftime.teacher.dto.TeacherUpdateRequest;
import com.example.talentoftime.teacher.repository.TeacherRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    public List<TeacherResponse> findAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(TeacherResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeacherResponse findTeacher(Long teacherId) {
        Teacher teacher = findTeacherOrThrow(teacherId);
        return TeacherResponse.from(teacher);
    }

    @Transactional
    public TeacherResponse createTeacher(TeacherCreateRequest request) {
        Teacher teacher = Teacher.create(
                request.getName(),
                request.getChalkType(),
                request.getChalkDetail(),
                request.getEraserDetail(),
                request.getMicType(),
                request.isHasPpt(),
                request.getPptDetail(),
                request.getNotes(),
                request.getLectureEmailRecipient());
        teacherRepository.save(teacher);
        log.info("강사 생성 완료: name={}", teacher.getName());
        return TeacherResponse.from(teacher);
    }

    @Transactional
    public TeacherResponse updateTeacher(Long teacherId, TeacherUpdateRequest request) {
        Teacher teacher = findTeacherOrThrow(teacherId);
        teacher.update(
                request.getName(),
                request.getChalkType(),
                request.getChalkDetail(),
                request.getEraserDetail(),
                request.getMicType(),
                request.isHasPpt(),
                request.getPptDetail(),
                request.getNotes(),
                request.getLectureEmailRecipient());
        log.info("강사 수정 완료: teacherId={}", teacherId);
        return TeacherResponse.from(teacher);
    }

    @Transactional
    public void deleteTeacher(Long teacherId) {
        Teacher teacher = findTeacherOrThrow(teacherId);
        teacherRepository.delete(teacher);
        log.info("강사 삭제 완료: teacherId={}", teacherId);
    }

    private Teacher findTeacherOrThrow(Long teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEACHER_NOT_FOUND));
    }
}
