package com.example.talentoftime.classsession.repository;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.period.domain.Period;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {

    List<ClassSession> findByDate(LocalDate date);

    boolean existsByDateAndPeriodAndClassroom(
            LocalDate date,
            Period period,
            Classroom classroom
    );

    Optional<ClassSession> findByDateAndPeriodAndClassroom(
            LocalDate date,
            Period period,
            Classroom classroom
    );

    @Query("SELECT cs FROM ClassSession cs " +
           "WHERE cs.date = :date " +
           "AND cs.period.startTime BETWEEN :from AND :to " +
           "AND cs.status <> com.example.talentoftime.classsession.domain.ClassStatus.CANCELLED")
    List<ClassSession> findUpcomingSessionsStartingBetween(
            @Param("date") LocalDate date,
            @Param("from") LocalTime from,
            @Param("to") LocalTime to
    );
}
