package com.example.talentoftime.crew.repository;

import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Long> {

    List<Crew> findByCrewType(CrewType crewType);
}
