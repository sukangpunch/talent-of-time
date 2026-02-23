package com.example.talentoftime.crew.repository;

import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {

    List<Crew> findByCrewType(CrewType crewType);

    boolean existsByName(String name);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Crew> findByUsername(String username);
}
