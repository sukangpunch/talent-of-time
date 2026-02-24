package com.example.talentoftime.auth.service;

import com.example.talentoftime.auth.domain.AccessToken;
import com.example.talentoftime.auth.domain.Subject;
import com.example.talentoftime.auth.token.JwtProvider;
import com.example.talentoftime.auth.token.config.TokenProperties;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.repository.CrewRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthTokenProvider {

    private static final String ROLE_CLAIM_KEY = "role";

    private final JwtProvider jwtProvider;
    private final CrewRepository crewRepository;
    private final TokenProperties tokenProperties;

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

    public void validateToken(String token) {
        jwtProvider.getClaims(token);
    }

    public Authentication getAuthUser(String token) {
        Crew crew = parseUser(token);
        return jwtProvider.getAuthentication(crew);
    }

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
