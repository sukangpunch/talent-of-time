package com.example.talentoftime.count.domain;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.crew.domain.Crew;
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
@Table(name = "count")
public class Count {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "count_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;

    @Column(name = "count", nullable = false)
    private int count;

    public static Count createInitial(Crew crew, TaskType taskType) {
        Count count = new Count();
        count.crew = crew;
        count.taskType = taskType;
        count.count = 0;
        return count;
    }

    public void increment() {
        this.count++;
    }

    public void decrement() {
        this.count = Math.max(0, this.count - 1);
    }

    public void reset() {
        this.count = 0;
    }
}
