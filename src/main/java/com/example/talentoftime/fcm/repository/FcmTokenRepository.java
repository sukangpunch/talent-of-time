package com.example.talentoftime.fcm.repository;

import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.fcm.domain.FcmToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    Optional<FcmToken> findByToken(String token);

    Optional<FcmToken> findByCrewId(Long crewId);

    List<FcmToken> findAllByCrewId(Long crewId);

    List<FcmToken> findAllByCrewCrewTypeIn(List<CrewType> crewTypes);
}
