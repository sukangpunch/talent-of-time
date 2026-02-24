package com.example.talentoftime.classsession.domain;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.teacher.domain.Teacher;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "class_session")
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_session_id")
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "cancelled", nullable = false)
    private boolean cancelled = false;

    private ClassSession(LocalDate date, Period period, Classroom classroom, Teacher teacher) {
        this.date = date;
        this.period = period;
        this.classroom = classroom;
        this.teacher = teacher;
    }

    public static ClassSession create(LocalDate date, Period period, Classroom classroom) {
        return new ClassSession(date, period, classroom, null);
    }

    public static ClassSession create(LocalDate date, Period period, Classroom classroom, Teacher teacher) {
        return new ClassSession(date, period, classroom, teacher);
    }

    public void update(LocalDate date, Period period, Classroom classroom) {
        this.date = date;
        this.period = period;
        this.classroom = classroom;
    }

    public void updateTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
