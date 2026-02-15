package com.example.talentoftime.schedule.domain;

import java.time.LocalDate;
import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.period.domain.Period;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    public static Schedule create(
            LocalDate date,
            Period period,
            Classroom classroom,
            TaskType taskType,
            Crew crew) {
        Schedule schedule = new Schedule();
        schedule.date = date;
        schedule.period = period;
        schedule.classroom = classroom;
        schedule.taskType = taskType;
        schedule.crew = crew;
        return schedule;
    }

    public void assignCrew(Crew crew) {
        this.crew = crew;
    }
}
