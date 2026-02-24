package com.example.talentoftime.crew.repository;

import com.example.talentoftime.crew.domain.Crew;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {

    Optional<Crew> findByName(String name);


    Optional<Crew> findByProviderId(String providerId);
}
