package com.example.talentoftime.classsession.repository;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.period.domain.Period;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {

    List<ClassSession> findByDate(LocalDate date);

    List<ClassSession> findByDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByDateAndPeriodAndClassroom(
            LocalDate date,
            Period period,
            Classroom classroom);

    Optional<ClassSession> findByDateAndPeriodAndClassroom(
            LocalDate date,
            Period period,
            Classroom classroom);

    List<ClassSession> findByDateAndPeriod(LocalDate date, Period period);
}
