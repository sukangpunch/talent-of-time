package com.example.talentoftime.auth.service;

import com.example.talentoftime.auth.domain.AccessToken;
import com.example.talentoftime.auth.domain.RefreshToken;
import com.example.talentoftime.auth.domain.Subject;
import com.example.talentoftime.auth.repository.RefreshTokenRepository;
import com.example.talentoftime.auth.token.JwtProvider;
import com.example.talentoftime.auth.token.config.TokenProperties;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.repository.CrewRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthTokenProvider {

    private static final String ROLE_CLAIM_KEY = "role";
    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    private final JwtProvider jwtProvider;
    private final CrewRepository crewRepository;
    private final TokenProperties tokenProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AccessToken generateAccessToken(Crew crew) {
        Subject subject = toSubject(crew);
        String role = crew.getRole() != null ? crew.getRole().name() : "USER";
        String token = jwtProvider.generateToken(
                subject,
                Map.of(ROLE_CLAIM_KEY, role),
                tokenProperties.access().expireTime()
        );
        return new AccessToken(token);
    }

    @Transactional
    public String issueRefreshToken(Crew crew) {
        Subject subject = toSubject(crew);
        String token = jwtProvider.generateToken(subject, tokenProperties.refresh().expireTime());
        LocalDateTime expiresAt = LocalDateTime.now(SEOUL_ZONE)
                .plus(tokenProperties.refresh().expireTime());

        refreshTokenRepository.findByCrewId(crew.getId())
                .ifPresentOrElse(
                        existing -> existing.rotate(token, expiresAt),
                        () -> refreshTokenRepository.save(new RefreshToken(crew.getId(), token, expiresAt))
                );

        return token;
    }

    @Transactional(readOnly = true)
    public Long validateRefreshTokenAndGetCrewId(String token) {
        long crewId = jwtProvider.getCrewId(token);
        RefreshToken stored = refreshTokenRepository.findByCrewId(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!stored.getToken().equals(token)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        return crewId;
    }

    @Transactional(readOnly = true)
    public Crew validateRefreshTokenAndGetCrew(String token) {
        Long crewId = validateRefreshTokenAndGetCrewId(token);
        return crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
    }

    @Transactional
    public void deleteRefreshToken(Long crewId) {
        refreshTokenRepository.deleteByCrewId(crewId);
    }

    @Transactional
    public void validateToken(String token) {
        jwtProvider.getClaims(token);
    }

    @Transactional
    public Authentication getAuthUser(String token) {
        Crew crew = parseUser(token);
        return jwtProvider.getAuthentication(crew);
    }

    @Transactional
    public Crew parseUser(String token) {
        Subject subject = jwtProvider.parseSubject(token);
        long crewId = Long.parseLong(subject.value());
        return crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
    }

    private Subject toSubject(Crew crew) {
        return new Subject(crew.getId().toString());
    }
}
