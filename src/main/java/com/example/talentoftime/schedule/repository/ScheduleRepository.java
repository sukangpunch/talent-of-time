package com.example.talentoftime.schedule.repository;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.schedule.domain.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDate(LocalDate date);

    boolean existsByDateAndPeriodAndClassroom(LocalDate date, Period period, Classroom classroom);
}
