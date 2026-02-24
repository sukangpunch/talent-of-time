package com.example.talentoftime.schedule.service;

import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.classsession.repository.ClassSessionRepository;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.count.domain.Count;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.repository.CrewRepository;
import com.example.talentoftime.schedule.domain.Schedule;
import com.example.talentoftime.schedule.dto.ScheduleCreateRequest;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.repository.ScheduleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CountRepository countRepository;
    private final CrewRepository crewRepository;
    private final ClassSessionRepository classSessionRepository;

    @Transactional
    public ScheduleResponse selfRegister(Long crewId, ScheduleCreateRequest request) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));

        ClassSession classSession = classSessionRepository.findById(request.classSessionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASS_SESSION_NOT_FOUND));

        Count count = findCountOrThrow(crew, request.taskType());
        count.increment();

        Schedule schedule = new Schedule(
                classSession.getDate(),
                request.taskType(),
                crew,
                classSession
        );

        scheduleRepository.save(schedule);
        log.info("스케줄 자기 등록 완료: crewId={}, classSessionId={}, taskType={}", crewId, request.classSessionId(), request.taskType());
        return ScheduleResponse.from(schedule);
    }

    @Transactional
    public void cancelRegistration(Long crewId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getCrew().getId().equals(crewId)) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_OWNER);
        }

        Count count = findCountOrThrow(schedule.getCrew(), schedule.getTaskType());
        count.decrement();

        scheduleRepository.delete(schedule);
        log.info("스케줄 등록 취소 완료: scheduleId={}, crewId={}", scheduleId, crewId);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findByClassSessionAndTaskType(Long classSessionId, TaskType taskType) {
        ClassSession classSession = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASS_SESSION_NOT_FOUND));
        return scheduleRepository.findByClassSessionAndTaskType(classSession, taskType).stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    private Count findCountOrThrow(Crew crew, TaskType taskType) {
        return countRepository.findByCrewAndTaskType(crew, taskType)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUNT_NOT_FOUND));
    }
}
