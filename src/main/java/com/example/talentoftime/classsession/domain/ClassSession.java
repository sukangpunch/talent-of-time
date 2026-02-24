package com.example.talentoftime.classsession.domain;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.teacher.domain.Teacher;
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

    @Column(name = "subject")
    private String subject;

    @Column(name = "group_name")
    private String group;

    @Column(name = "in_person_count", nullable = false)
    private int inPersonCount;

    @Column(name = "online_count", nullable = false)
    private int onlineCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClassStatus status;

    public ClassSession(
            LocalDate date,
            Period period,
            Classroom classroom,
            Teacher teacher,
            String subject,
            String group,
            int inPersonCount,
            int onlineCount
    ) {
        this.date = date;
        this.period = period;
        this.classroom = classroom;
        this.teacher = teacher;
        this.subject = subject;
        this.group = group;
        this.inPersonCount = inPersonCount;
        this.onlineCount = onlineCount;
        this.status = ClassStatus.NORMAL;
    }

    public void update(Period period, Classroom classroom) {
        this.period = period;
        this.classroom = classroom;
    }

    public void updateTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void cancel() {
        this.status = ClassStatus.CANCELLED;
    }

    public boolean isCancelled() {
        return this.status == ClassStatus.CANCELLED;
    }
}
