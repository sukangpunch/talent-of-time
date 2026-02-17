package com.example.talentoftime.schedule.service;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classroom.repository.ClassroomRepository;
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
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.dto.ScheduleSwapRequest;
import com.example.talentoftime.schedule.repository.ScheduleRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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

        List<Classroom> classrooms = classroomRepository.findAll();
        List<Schedule> schedules = classrooms.stream()
                .map(classroom -> assignSingleSchedule(date, settingPeriod, classroom, TaskType.SETTING))
                .toList();

        scheduleRepository.saveAll(schedules);
        log.info("세팅 스케줄 자동 배정 완료: date={}, count={}", date, schedules.size());

        return schedules.stream()
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
            default -> throw new BusinessException(ErrorCode.SCHEDULE_INVALID_CREW_TYPE);
        };
    }

    private Crew selectCrewByLowestCount(List<CrewType> eligibleTypes, TaskType taskType) {
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

        int randomIndex = ThreadLocalRandom.current().nextInt(candidates.size());
        return candidates.get(randomIndex);
    }

    private Count findCountOrThrow(Crew crew, TaskType taskType) {
        return countRepository.findByCrewAndTaskType(crew, taskType)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUNT_NOT_FOUND));
    }
}
