package com.example.talentoftime.auth.service;

import com.example.talentoftime.auth.dto.LoginRequest;
import com.example.talentoftime.auth.dto.LoginResponse;
import com.example.talentoftime.auth.util.JwtTokenProvider;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.repository.CrewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final CrewRepository crewRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Crew crew = crewRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_INVALID_CREDENTIALS));

        if (crew.getPassword() == null || !passwordEncoder.matches(request.getPassword(), crew.getPassword())) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }

        String role = crew.getRole() != null ? crew.getRole().name() : "USER";
        String token = jwtTokenProvider.generateToken(crew.getId(), crew.getUsername(), role);
        log.info("로그인 성공: username={}", crew.getUsername());

        return LoginResponse.of(token, crew.getId(), crew.getName(), role);
    }
}
