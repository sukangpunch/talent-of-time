package com.example.talentoftime.auth.repository;

import com.example.talentoftime.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByCrewId(Long crewId);

    Optional<RefreshToken> findByToken(String token);

    void deleteByCrewId(Long crewId);
}
