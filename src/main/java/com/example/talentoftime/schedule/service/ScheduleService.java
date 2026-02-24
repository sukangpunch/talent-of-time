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
import com.example.talentoftime.schedule.dto.ScheduleSwapRequest;
import com.example.talentoftime.schedule.repository.ScheduleRepository;
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
    public void swapSchedules(ScheduleSwapRequest request) {
        Schedule scheduleA = scheduleRepository.findById(request.getScheduleIdA())
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));
        Schedule scheduleB = scheduleRepository.findById(request.getScheduleIdB())
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (scheduleA.getTaskType() == scheduleB.getTaskType()) {
            throw new BusinessException(ErrorCode.SCHEDULE_SWAP_SAME_TASK);
        }

        Crew crewA = scheduleA.getCrew();
        Crew crewB = scheduleB.getCrew();
        TaskType taskTypeA = scheduleA.getTaskType();
        TaskType taskTypeB = scheduleB.getTaskType();

        Count countAOriginal = findCountOrThrow(crewA, taskTypeA);
        countAOriginal.decrement();
        Count countANew = findCountOrThrow(crewA, taskTypeB);
        countANew.increment();

        Count countBOriginal = findCountOrThrow(crewB, taskTypeB);
        countBOriginal.decrement();
        Count countBNew = findCountOrThrow(crewB, taskTypeA);
        countBNew.increment();

        scheduleA.assignCrew(crewB);
        scheduleB.assignCrew(crewA);

        log.info("스케줄 교환 완료: scheduleIdA={}, scheduleIdB={}", scheduleA.getId(), scheduleB.getId());
    }

    @Transactional
    public ScheduleResponse selfRegister(Long crewId, ScheduleCreateRequest request) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));

        ClassSession classSession = classSessionRepository.findById(request.getClassSessionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASS_SESSION_NOT_FOUND));

        if (scheduleRepository.existsByClassSessionAndTaskType(classSession, request.getTaskType())) {
            throw new BusinessException(ErrorCode.SCHEDULE_DUPLICATE_TASK);
        }

        Count count = findCountOrThrow(crew, request.getTaskType());
        count.increment();

        Schedule schedule = Schedule.create(
                classSession.getDate(),
                classSession.getPeriod(),
                classSession.getClassroom(),
                request.getTaskType(),
                crew,
                classSession);

        scheduleRepository.save(schedule);
        log.info("스케줄 자기 등록 완료: crewId={}, classSessionId={}, taskType={}",
                crewId, request.getClassSessionId(), request.getTaskType());
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

    private Count findCountOrThrow(Crew crew, TaskType taskType) {
        return countRepository.findByCrewAndTaskType(crew, taskType)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUNT_NOT_FOUND));
    }
}
