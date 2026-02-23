package com.example.talentoftime.schedule.repository;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.schedule.domain.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDate(LocalDate date);

    boolean existsByDate(LocalDate date);

    List<Schedule> findByDateAndPeriod(LocalDate date, Period period);

    List<Schedule> findByDateAndCrew(LocalDate date, Crew crew);

    boolean existsByDateAndPeriodAndClassroomAndTaskType(
            LocalDate date,
            Period period,
            Classroom classroom,
            TaskType taskType);

    List<Schedule> findByClassSession(ClassSession classSession);

    void deleteByClassSession(ClassSession classSession);

    boolean existsByClassSessionAndTaskType(ClassSession classSession, TaskType taskType);
}
