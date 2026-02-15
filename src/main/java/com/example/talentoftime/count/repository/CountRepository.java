package com.example.talentoftime.count.repository;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.count.domain.Count;
import com.example.talentoftime.crew.domain.Crew;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountRepository extends JpaRepository<Count, Long> {

    Optional<Count> findByCrewAndTaskType(Crew crew, TaskType taskType);

    List<Count> findByCrew(Crew crew);

    List<Count> findByCrewInAndTaskType(List<Crew> crews, TaskType taskType);

    List<Count> findByTaskType(TaskType taskType);
}
