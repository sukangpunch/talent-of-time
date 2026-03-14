package com.example.talentoftime.crew.repository;

import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {

    Optional<Crew> findByName(String name);

    Optional<Crew> findByProviderId(String providerId);

    List<Crew> findAllByFcmTokenIsNotNull();

    List<Crew> findAllByFcmTokenIsNotNullAndCrewTypeIn(List<CrewType> crewTypes);
}
