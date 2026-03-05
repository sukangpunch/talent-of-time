package com.example.talentoftime.auth.controller;

import com.example.talentoftime.auth.service.AuthTokenProvider;
import com.example.talentoftime.auth.token.config.TokenProperties;
import com.example.talentoftime.auth.util.CookieUtil;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.crew.domain.Crew;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthTokenProvider authTokenProvider;
    private final CookieUtil cookieUtil;
    private final TokenProperties tokenProperties;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = cookieUtil.extractRefreshToken(request);
        if (refreshToken == null) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        Crew crew = authTokenProvider.validateRefreshTokenAndGetCrew(refreshToken);
        String newAccessToken = authTokenProvider.generateAccessToken(crew).token();
        String newRefreshToken = authTokenProvider.issueRefreshToken(crew);

        cookieUtil.setAccessTokenCookie(response, newAccessToken, tokenProperties.access().expireTime());
        cookieUtil.setRefreshTokenCookie(response, newRefreshToken, tokenProperties.refresh().expireTime());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = cookieUtil.extractRefreshToken(request);
        if (refreshToken != null) {
            try {
                Long crewId = authTokenProvider.validateRefreshTokenAndGetCrewId(refreshToken);
                authTokenProvider.deleteRefreshToken(crewId);
            } catch (BusinessException ignored) {
                // 이미 만료/무효 토큰이어도 쿠키는 삭제
            }
        }

        cookieUtil.clearAccessTokenCookie(response);
        cookieUtil.clearRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }
}
