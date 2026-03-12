package com.example.talentoftime.teacher.service;

import com.example.talentoftime.cache.annotation.DefaultCacheOut;
import com.example.talentoftime.cache.annotation.DefaultCaching;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.teacher.domain.Teacher;
import com.example.talentoftime.teacher.dto.AllTeacherResponse;
import com.example.talentoftime.teacher.dto.TeacherCreateRequest;
import com.example.talentoftime.teacher.dto.TeacherResponse;
import com.example.talentoftime.teacher.dto.TeacherSearchResponse;
import com.example.talentoftime.teacher.dto.TeacherSimpleDto;
import com.example.talentoftime.teacher.dto.TeacherUpdateRequest;
import com.example.talentoftime.teacher.repository.TeacherRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TeacherService {

    private static final String CACHE_MANAGER = "customCacheManager";
    private static final String TEACHER_KEY_PREFIX = "teacher:";

    private final TeacherRepository teacherRepository;

    @DefaultCaching(key = "teacher:all", cacheManager = CACHE_MANAGER, ttlSec = 1800)
    @Transactional(readOnly = true)
    public AllTeacherResponse findAllTeachers() {
        return new AllTeacherResponse(
                teacherRepository.findAllByOrderByNameAsc().stream()
                        .map(TeacherResponse::from)
                        .toList()
        );
    }

    @DefaultCaching(key = "teacher:{0}", cacheManager = CACHE_MANAGER, ttlSec = 3600)
    @Transactional(readOnly = true)
    public TeacherResponse findTeacher(Long teacherId) {
        Teacher teacher = findTeacherOrThrow(teacherId);
        return TeacherResponse.from(teacher);
    }

    @DefaultCacheOut(key = {TEACHER_KEY_PREFIX}, cacheManager = CACHE_MANAGER, prefix = true)
    @Transactional
    public TeacherResponse createTeacher(TeacherCreateRequest request) {
        Teacher teacher = new Teacher(
                request.name(),
                request.chalkType(),
                request.chalkDetail(),
                request.eraserDetail(),
                request.micType(),
                request.hasPpt(),
                request.notes(),
                request.email()
        );
        teacherRepository.save(teacher);
        log.info("강사 생성 완료: name={}", teacher.getName());
        return TeacherResponse.from(teacher);
    }

    @DefaultCacheOut(key = {TEACHER_KEY_PREFIX}, cacheManager = CACHE_MANAGER, prefix = true)
    @Transactional
    public TeacherResponse updateTeacher(Long teacherId, TeacherUpdateRequest request) {
        Teacher teacher = findTeacherOrThrow(teacherId);
        teacher.update(
                request.name(),
                request.chalkType(),
                request.chalkDetail(),
                request.eraserDetail(),
                request.micType(),
                request.hasPpt(),
                request.notes(),
                request.email()
        );
        log.info("강사 수정 완료: teacherId={}", teacherId);
        return TeacherResponse.from(teacher);
    }

    @DefaultCacheOut(key = {TEACHER_KEY_PREFIX}, cacheManager = CACHE_MANAGER, prefix = true)
    @Transactional
    public void deleteTeacher(Long teacherId) {
        Teacher teacher = findTeacherOrThrow(teacherId);
        teacherRepository.delete(teacher);
        log.info("강사 삭제 완료: teacherId={}", teacherId);
    }

    @DefaultCaching(key = "teacher:search:{0}", cacheManager = CACHE_MANAGER, ttlSec = 600)
    @Transactional(readOnly = true)
    public TeacherSearchResponse searchTeachersByName(String name) {
        List<Teacher> teachers = teacherRepository.findByNameStartingWith(name);
        boolean hasExactMatch = teachers.stream().anyMatch(t -> t.getName().equals(name));
        if (hasExactMatch) {
            return new TeacherSearchResponse(
                    teachers.stream()
                            .filter(t -> t.getName().equals(name))
                            .map(TeacherSimpleDto::from)
                            .toList()
            );
        }
        return new TeacherSearchResponse(
                teachers.stream()
                        .map(TeacherSimpleDto::from)
                        .toList()
        );
    }

    private Teacher findTeacherOrThrow(Long teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEACHER_NOT_FOUND));
    }
}
