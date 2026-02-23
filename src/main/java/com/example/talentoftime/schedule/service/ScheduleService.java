package com.example.talentoftime.schedule.service;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classroom.repository.ClassroomRepository;
import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.classsession.repository.ClassSessionRepository;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.count.domain.Count;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.crew.repository.CrewRepository;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.period.repository.PeriodRepository;
import com.example.talentoftime.schedule.domain.Schedule;
import com.example.talentoftime.schedule.dto.ScheduleCreateRequest;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.dto.ScheduleSwapRequest;
import com.example.talentoftime.schedule.repository.ScheduleRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
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
    private final PeriodRepository periodRepository;
    private final ClassroomRepository classroomRepository;
    private final ClassSessionRepository classSessionRepository;

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findSchedulesByDate(LocalDate date) {
        return scheduleRepository.findByDate(date).stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findSchedulesByDateAndPeriod(LocalDate date, int periodNumber) {
        Period period = periodRepository.findByPeriodNumber(periodNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.PERIOD_NOT_FOUND));
        return scheduleRepository.findByDateAndPeriod(date, period).stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    @Transactional
    public List<ScheduleResponse> assignSettingSchedules(LocalDate date) {
        Period settingPeriod = periodRepository.findByPeriodNumber(0)
                .orElseThrow(() -> new BusinessException(ErrorCode.PERIOD_NOT_FOUND));

        List<ClassSession> sessions = classSessionRepository.findByDate(date);
        if (sessions.isEmpty()) {
            throw new BusinessException(ErrorCode.CLASS_SESSION_EMPTY_FOR_DATE);
        }
        List<Classroom> classrooms = sessions.stream()
                .map(ClassSession::getClassroom)
                .distinct()
                .toList();

        List<Schedule> schedules = classrooms.stream()
                .map(classroom -> assignSingleSchedule(date, settingPeriod, classroom, TaskType.SETTING))
                .toList();

        scheduleRepository.saveAll(schedules);
        log.info("세팅 스케줄 자동 배정 완료: date={}, count={}", date, schedules.size());

        return schedules.stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    @Transactional
    public List<ScheduleResponse> assignDailySchedules(LocalDate date) {
        List<ClassSession> sessions = classSessionRepository.findByDate(date);
        if (sessions.isEmpty()) {
            throw new BusinessException(ErrorCode.CLASS_SESSION_EMPTY_FOR_DATE);
        }

        if (scheduleRepository.existsByDate(date)) {
            throw new BusinessException(ErrorCode.SCHEDULE_ALREADY_ASSIGNED);
        }

        // 4~6교시는 현재 스케줄링 대상에서 제외 (1~3교시만 배정)
        List<ClassSession> schedulableSessions = sessions.stream()
                .filter(session -> session.getPeriod().getPeriodNumber() <= 3)
                .toList();

        Period settingPeriod = periodRepository.findByPeriodNumber(0)
                .orElseThrow(() -> new BusinessException(ErrorCode.PERIOD_NOT_FOUND));

        List<Classroom> classrooms = sessions.stream()
                .map(ClassSession::getClassroom)
                .distinct()
                .toList();

        List<Schedule> settingSchedules = classrooms.stream()
                .map(classroom -> assignSingleSchedule(date, settingPeriod, classroom, TaskType.SETTING))
                .toList();

        // 같은 교시(시간대)에 걸쳐 busyCrewIds를 공유해 동일 시간대 중복 배정을 최소화
        // TreeMap으로 교시 번호 오름차순 처리 보장
        Map<Integer, List<ClassSession>> sessionsByPeriod = schedulableSessions.stream()
                .collect(Collectors.groupingBy(
                        session -> session.getPeriod().getPeriodNumber(),
                        TreeMap::new,
                        Collectors.toList()));

        List<TaskType> periodTaskTypes = List.of(TaskType.ENTRY, TaskType.JOG, TaskType.EXIT);
        List<Schedule> periodSchedules = new ArrayList<>();
        for (List<ClassSession> periodSessions : sessionsByPeriod.values()) {
            Set<Long> busyCrewIds = new HashSet<>();
            for (ClassSession session : periodSessions) {
                for (TaskType taskType : periodTaskTypes) {
                    Schedule schedule = assignSingleScheduleLinked(session, taskType, busyCrewIds);
                    busyCrewIds.add(schedule.getCrew().getId());
                    periodSchedules.add(schedule);
                }
            }
        }

        List<Schedule> allSchedules = new ArrayList<>();
        allSchedules.addAll(settingSchedules);
        allSchedules.addAll(periodSchedules);

        scheduleRepository.saveAll(allSchedules);
        log.info("일별 스케줄 자동 배정 완료: date={}, total={}", date, allSchedules.size());

        return allSchedules.stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    private Schedule assignSingleSchedule(
            LocalDate date,
            Period period,
            Classroom classroom,
            TaskType taskType) {
        List<CrewType> eligibleTypes = getEligibleCrewTypes(period.getPeriodNumber(), taskType);
        Crew crew = selectCrewByLowestCount(eligibleTypes, taskType);

        Count count = findCountOrThrow(crew, taskType);
        count.increment();

        return Schedule.create(date, period, classroom, taskType, crew);
    }

    private Schedule assignSingleScheduleLinked(
            ClassSession classSession,
            TaskType taskType,
            Set<Long> busyCrewIds) {
        List<CrewType> eligibleTypes = getEligibleCrewTypes(
                classSession.getPeriod().getPeriodNumber(), taskType);
        Crew crew = selectCrewByLowestCount(eligibleTypes, taskType, busyCrewIds);

        Count count = findCountOrThrow(crew, taskType);
        count.increment();

        return Schedule.create(
                classSession.getDate(),
                classSession.getPeriod(),
                classSession.getClassroom(),
                taskType,
                crew,
                classSession);
    }

    @Transactional
    public List<ScheduleResponse> assignPeriodSchedules(LocalDate date, int periodNumber, Long classroomId) {
        Period period = periodRepository.findByPeriodNumber(periodNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.PERIOD_NOT_FOUND));
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASSROOM_NOT_FOUND));

        List<TaskType> periodTaskTypes = List.of(TaskType.ENTRY, TaskType.JOG, TaskType.EXIT);

        List<Schedule> schedules = periodTaskTypes.stream()
                .map(taskType -> {
                    if (scheduleRepository.existsByDateAndPeriodAndClassroomAndTaskType(
                            date, period, classroom, taskType)) {
                        throw new BusinessException(ErrorCode.SCHEDULE_ALREADY_ASSIGNED);
                    }
                    return assignSingleSchedule(date, period, classroom, taskType);
                })
                .toList();

        scheduleRepository.saveAll(schedules);
        log.info("교시 스케줄 자동 배정 완료: date={}, periodNumber={}, classroomId={}", date, periodNumber, classroomId);

        return schedules.stream()
                .map(ScheduleResponse::from)
                .toList();
    }

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

    private List<CrewType> getEligibleCrewTypes(int periodNumber, TaskType taskType) {
        if (taskType == TaskType.SETTING) {
            return List.of(CrewType.MORNING);
        }

        return switch (periodNumber) {
            case 1 -> switch (taskType) {
                case ENTRY, JOG -> List.of(CrewType.MORNING);
                case EXIT -> List.of(CrewType.MORNING, CrewType.MIDDLE);
                default -> throw new BusinessException(ErrorCode.SCHEDULE_INVALID_CREW_TYPE);
            };
            case 2 -> List.of(CrewType.MORNING, CrewType.MIDDLE);
            case 3 -> switch (taskType) {
                case ENTRY, JOG -> List.of(CrewType.MORNING, CrewType.MIDDLE);
                case EXIT -> List.of(CrewType.AFTERNOON);
                default -> throw new BusinessException(ErrorCode.SCHEDULE_INVALID_CREW_TYPE);
            };
            case 4 -> List.of(CrewType.MIDDLE, CrewType.AFTERNOON);
            case 5, 6 -> List.of(CrewType.AFTERNOON);
            default -> throw new BusinessException(ErrorCode.SCHEDULE_INVALID_CREW_TYPE);
        };
    }

    private Crew selectCrewByLowestCount(List<CrewType> eligibleTypes, TaskType taskType) {
        return selectCrewByLowestCount(eligibleTypes, taskType, Set.of());
    }

    private Crew selectCrewByLowestCount(
            List<CrewType> eligibleTypes,
            TaskType taskType,
            Set<Long> busyCrewIds) {
        List<Crew> eligibleCrews = eligibleTypes.stream()
                .flatMap(crewType -> crewRepository.findByCrewType(crewType).stream())
                .toList();

        if (eligibleCrews.isEmpty()) {
            throw new BusinessException(ErrorCode.SCHEDULE_NO_ELIGIBLE_CREW);
        }

        List<Count> counts = countRepository.findByCrewInAndTaskType(eligibleCrews, taskType);

        int minCount = counts.stream()
                .mapToInt(Count::getCount)
                .min()
                .orElse(0);

        List<Crew> candidates = counts.stream()
                .filter(count -> count.getCount() == minCount)
                .map(Count::getCrew)
                .toList();

        if (candidates.isEmpty()) {
            throw new BusinessException(ErrorCode.SCHEDULE_NO_ELIGIBLE_CREW);
        }

        return candidates.stream()
                .sorted(Comparator
                        .comparing((Crew c) -> busyCrewIds.contains(c.getId()))
                        .thenComparing(Crew::getName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NO_ELIGIBLE_CREW));
    }

    private Count findCountOrThrow(Crew crew, TaskType taskType) {
        return countRepository.findByCrewAndTaskType(crew, taskType)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUNT_NOT_FOUND));
    }
}
