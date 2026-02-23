package com.example.talentoftime.classsession.service;

import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.classsession.dto.ClassSessionBulkCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionResponse;
import com.example.talentoftime.classsession.dto.ClassSessionUpdateRequest;
import com.example.talentoftime.classsession.repository.ClassSessionRepository;
import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classroom.repository.ClassroomRepository;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.count.domain.Count;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.period.repository.PeriodRepository;
import com.example.talentoftime.schedule.domain.Schedule;
import com.example.talentoftime.schedule.repository.ScheduleRepository;
import com.example.talentoftime.teacher.domain.Teacher;
import com.example.talentoftime.teacher.repository.TeacherRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassSessionService {

    private final ClassSessionRepository classSessionRepository;
    private final PeriodRepository periodRepository;
    private final ClassroomRepository classroomRepository;
    private final ScheduleRepository scheduleRepository;
    private final CountRepository countRepository;
    private final TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    public ClassSessionResponse findClassSession(Long classSessionId) {
        ClassSession classSession = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASS_SESSION_NOT_FOUND));
        return ClassSessionResponse.from(classSession);
    }

    @Transactional(readOnly = true)
    public List<ClassSessionResponse> findClassSessionsByDate(LocalDate date) {
        return classSessionRepository.findByDate(date).stream()
                .map(ClassSessionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClassSessionResponse> findTodayClassSessions() {
        return findClassSessionsByDate(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<ClassSessionResponse> findWeeklyClassSessions(LocalDate date) {
        LocalDate monday = date.with(DayOfWeek.MONDAY);
        LocalDate sunday = date.with(DayOfWeek.SUNDAY);
        return classSessionRepository.findByDateBetween(monday, sunday).stream()
                .map(ClassSessionResponse::from)
                .toList();
    }

    @Transactional
    public ClassSessionResponse createClassSession(ClassSessionCreateRequest request) {
        Period period = findPeriodOrThrow(request.getPeriodNumber());
        Classroom classroom = findClassroomOrThrow(request.getClassroomId());
        validateNoDuplicate(request.getDate(), period, classroom);

        Teacher teacher = findTeacherIfPresent(request.getTeacherId());
        ClassSession classSession = ClassSession.create(request.getDate(), period, classroom, teacher);
        classSessionRepository.save(classSession);
        log.info("수업 일정 단건 등록 완료: date={}, periodNumber={}, classroomId={}",
                request.getDate(), request.getPeriodNumber(), request.getClassroomId());
        return ClassSessionResponse.from(classSession);
    }

    @Transactional
    public List<ClassSessionResponse> createBulkClassSessions(ClassSessionBulkCreateRequest request) {
        List<ClassSession> sessions = request.getSessions().stream()
                .map(item -> {
                    Period period = findPeriodOrThrow(item.getPeriodNumber());
                    Classroom classroom = findClassroomOrThrow(item.getClassroomId());
                    validateNoDuplicate(item.getDate(), period, classroom);
                    Teacher teacher = findTeacherIfPresent(item.getTeacherId());
                    return ClassSession.create(item.getDate(), period, classroom, teacher);
                })
                .toList();

        classSessionRepository.saveAll(sessions);
        log.info("수업 일정 일괄 등록 완료: count={}", sessions.size());
        return sessions.stream()
                .map(ClassSessionResponse::from)
                .toList();
    }

    @Transactional
    public ClassSessionResponse updateClassSession(
            Long classSessionId,
            ClassSessionUpdateRequest request) {
        ClassSession classSession = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASS_SESSION_NOT_FOUND));

        Period newPeriod = findPeriodOrThrow(request.getPeriodNumber());
        Classroom newClassroom = findClassroomOrThrow(request.getClassroomId());

        classSessionRepository.findByDateAndPeriodAndClassroom(
                        request.getDate(), newPeriod, newClassroom)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(classSessionId)) {
                        throw new BusinessException(ErrorCode.CLASS_SESSION_DUPLICATED);
                    }
                });

        deleteLinkedSchedules(classSession);

        classSession.update(request.getDate(), newPeriod, newClassroom);
        Teacher teacher = findTeacherIfPresent(request.getTeacherId());
        classSession.updateTeacher(teacher);
        log.info("수업 일정 수정 완료: classSessionId={}", classSessionId);
        return ClassSessionResponse.from(classSession);
    }

    @Transactional
    public void deleteClassSession(Long classSessionId) {
        ClassSession classSession = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASS_SESSION_NOT_FOUND));

        deleteLinkedSchedules(classSession);

        classSessionRepository.delete(classSession);
        log.info("수업 일정 삭제 완료: classSessionId={}", classSessionId);
    }

    private void deleteLinkedSchedules(ClassSession classSession) {
        List<Schedule> linkedSchedules = scheduleRepository.findByClassSession(classSession);
        for (Schedule schedule : linkedSchedules) {
            recoverCount(schedule.getCrew(), schedule.getTaskType());
        }
        scheduleRepository.deleteByClassSession(classSession);
        if (!linkedSchedules.isEmpty()) {
            log.info("연결된 스케줄 삭제 및 count 복구 완료: classSessionId={}, scheduleCount={}",
                    classSession.getId(), linkedSchedules.size());
        }
    }

    private void recoverCount(Crew crew, TaskType taskType) {
        countRepository.findByCrewAndTaskType(crew, taskType)
                .ifPresent(Count::decrement);
    }

    private Period findPeriodOrThrow(int periodNumber) {
        return periodRepository.findByPeriodNumber(periodNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.PERIOD_NOT_FOUND));
    }

    private Classroom findClassroomOrThrow(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASSROOM_NOT_FOUND));
    }

    private Teacher findTeacherIfPresent(Long teacherId) {
        if (teacherId == null) {
            return null;
        }
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEACHER_NOT_FOUND));
    }

    private void validateNoDuplicate(LocalDate date, Period period, Classroom classroom) {
        if (classSessionRepository.existsByDateAndPeriodAndClassroom(date, period, classroom)) {
            throw new BusinessException(ErrorCode.CLASS_SESSION_DUPLICATED);
        }
    }
}
