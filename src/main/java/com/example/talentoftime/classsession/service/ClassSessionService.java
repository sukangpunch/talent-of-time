package com.example.talentoftime.classsession.service;

import com.example.talentoftime.cache.annotation.DefaultCacheOut;
import com.example.talentoftime.cache.annotation.DefaultCaching;
import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.classsession.dto.ClassSessionBulkCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionResponse;
import com.example.talentoftime.classsession.dto.ClassSessionUpdateRequest;
import com.example.talentoftime.classsession.dto.DayByClassSessionResponse;
import com.example.talentoftime.classsession.repository.ClassSessionRepository;
import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classroom.repository.ClassroomRepository;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.period.repository.PeriodRepository;
import com.example.talentoftime.teacher.domain.Teacher;
import com.example.talentoftime.teacher.repository.TeacherRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassSessionService {

    private static final String CACHE_MANAGER = "customCacheManager";
    private static final String CLASS_SESSION_KEY_PREFIX = "class-session:";

    private final ClassSessionRepository classSessionRepository;
    private final PeriodRepository periodRepository;
    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;

    @DefaultCaching(key = "class-session:{0}", cacheManager = CACHE_MANAGER, ttlSec = 3600)
    @Transactional(readOnly = true)
    public ClassSessionResponse findClassSession(Long classSessionId) {
        ClassSession classSession = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASS_SESSION_NOT_FOUND));
        return ClassSessionResponse.from(classSession);
    }

    @DefaultCaching(key = "'class-session:' + #date.toString()" , cacheManager = CACHE_MANAGER, ttlSec = 43200) // 12시간
    @Transactional(readOnly = true)
    public DayByClassSessionResponse findClassSessionsByDate(LocalDate date) {
        return new DayByClassSessionResponse(
                classSessionRepository.findByDate(date).stream()
                        .map(ClassSessionResponse::from)
                        .toList()
        );
    }

    @DefaultCaching(key = "'class-session:' + T(java.time.LocalDate).now().toString()", cacheManager = CACHE_MANAGER, ttlSec = 43200)
    @Transactional(readOnly = true)
    public DayByClassSessionResponse findTodayClassSessions() {
        return new DayByClassSessionResponse(
                classSessionRepository.findByDate(LocalDate.now()).stream()
                        .map(ClassSessionResponse::from)
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public DayByClassSessionResponse findLastWeekSameDayClassSessions(LocalDate date) {
        return new DayByClassSessionResponse(
                classSessionRepository.findByDate(date.minusWeeks(1)).stream()
                        .map(ClassSessionResponse::from)
                        .toList()
        );
    }

    @DefaultCacheOut(key = {CLASS_SESSION_KEY_PREFIX}, cacheManager = CACHE_MANAGER, prefix = true)
    @Transactional
    public DayByClassSessionResponse createBulkClassSessions(ClassSessionBulkCreateRequest request) {
        validateNoDuplicateInRequest(request.sessions());
        List<ClassSession> sessions = request.sessions().stream()
                .map(item -> {
                    Period period = findPeriodOrThrow(item.periodNumber());
                    Classroom classroom = findClassroomOrThrow(item.classroomId());
                    validateNoDuplicate(item.date(), period, classroom);
                    Teacher teacher = findTeacherIfPresent(item.teacherName());
                    return new ClassSession(
                            item.date(),
                            period,
                            classroom,
                            teacher,
                            item.subject(),
                            item.group(),
                            item.inPersonCount(),
                            item.onlineCount(),
                            item.classStatus()
                    );
                })
                .toList();

        classSessionRepository.saveAll(sessions);
        log.info("수업 일정 일괄 등록 완료: count={}", sessions.size());
        return new DayByClassSessionResponse(sessions.stream()
                .map(ClassSessionResponse::from)
                .toList()
        );
    }

    @DefaultCacheOut(key = {CLASS_SESSION_KEY_PREFIX}, cacheManager = CACHE_MANAGER, prefix = true)
    public ClassSessionResponse updateClassSession(
            Long classSessionId,
            ClassSessionUpdateRequest request
    ) {
        ClassSession classSession = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASS_SESSION_NOT_FOUND));

        Period newPeriod = request.periodNumber() != null
                ? findPeriodOrThrow(request.periodNumber())
                : classSession.getPeriod();
        Classroom newClassroom = request.classroomId() != null
                ? findClassroomOrThrow(request.classroomId())
                : classSession.getClassroom();

        classSessionRepository.findByDateAndPeriodAndClassroom(
                        classSession.getDate(),
                        newPeriod,
                        newClassroom
                )
                .ifPresent(existing -> {
                    if (!existing.getId().equals(classSessionId)) {
                        throw new BusinessException(ErrorCode.CLASS_SESSION_DUPLICATED);
                    }
                });

        classSession.update(newPeriod, newClassroom);
        log.info("수업 일정 수정 완료: classSessionId={}", classSessionId);
        return ClassSessionResponse.from(classSession);
    }

    @DefaultCacheOut(key = {CLASS_SESSION_KEY_PREFIX}, cacheManager = CACHE_MANAGER, prefix = true)
    @Transactional
    public void deleteClassSessionsByDate(LocalDate date) {
        classSessionRepository.deleteByDate(date);
        log.info("수업 일정 일괄 삭제 완료: date={}", date);
    }

    @DefaultCacheOut(key = {CLASS_SESSION_KEY_PREFIX}, cacheManager = CACHE_MANAGER, prefix = true)
    @Transactional
    public ClassSessionResponse cancelClassSession(Long classSessionId) {
        ClassSession classSession = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASS_SESSION_NOT_FOUND));

        if (classSession.isCancelled()) {
            throw new BusinessException(ErrorCode.CLASS_SESSION_ALREADY_CANCELLED);
        }

        classSession.cancel();
        log.info("수업 일정 휴강 처리 완료: classSessionId={}", classSessionId);
        return ClassSessionResponse.from(classSession);
    }

    private Period findPeriodOrThrow(int periodNumber) {
        return periodRepository.findByPeriodNumber(periodNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.PERIOD_NOT_FOUND));
    }

    private Classroom findClassroomOrThrow(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASSROOM_NOT_FOUND));
    }

    private Teacher findTeacherIfPresent(String teacherName) {
        if (teacherName == null) {
            return null;
        }
        return teacherRepository.findFirstByName(teacherName)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEACHER_NOT_FOUND));
    }

    private void validateNoDuplicateInRequest(List<ClassSessionCreateRequest> sessions) {
        Set<String> keys = new HashSet<>();
        for (ClassSessionCreateRequest item : sessions) {
            String key = item.date() + "-" + item.periodNumber() + "-" + item.classroomId();
            if (!keys.add(key)) {
                throw new BusinessException(ErrorCode.CLASS_SESSION_DUPLICATED);
            }
        }
    }

    private void validateNoDuplicate(LocalDate date, Period period, Classroom classroom) {
        if (classSessionRepository.existsByDateAndPeriodAndClassroom(date, period, classroom)) {
            throw new BusinessException(ErrorCode.CLASS_SESSION_DUPLICATED);
        }
    }
}
