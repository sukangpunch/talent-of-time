package com.example.talentoftime.schedule.repository;

import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.schedule.domain.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByClassSession(ClassSession classSession);

    void deleteByClassSession(ClassSession classSession);

    List<Schedule> findByClassSessionAndTaskType(ClassSession classSession, TaskType taskType);
}
