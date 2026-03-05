package com.example.talentoftime.auth.controller;

import com.example.talentoftime.auth.dto.LoginResponse;
import com.example.talentoftime.auth.dto.LoginResult;
import com.example.talentoftime.auth.dto.LoginUrlResponse;
import com.example.talentoftime.auth.dto.oauth.OAuthUserInfo;
import com.example.talentoftime.auth.service.oauth.OAuthLoginProcessor;
import com.example.talentoftime.auth.service.oauth.OAuthLoginUrlService;
import com.example.talentoftime.auth.service.oauth.OAuthUserInfoService;
import com.example.talentoftime.auth.token.config.TokenProperties;
import com.example.talentoftime.auth.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthUserInfoService oAuthUserInfoService;
    private final OAuthLoginProcessor oAuthLoginProcessor;
    private final OAuthLoginUrlService oAuthLoginUrlService;
    private final CookieUtil cookieUtil;
    private final TokenProperties tokenProperties;

    @GetMapping("/login-url")
    public ResponseEntity<LoginUrlResponse> authorizeUrl(
            @RequestParam String dest
    ) {
        String loginUrl = oAuthLoginUrlService.generateLoginUrl(dest);
        return ResponseEntity.ok(new LoginUrlResponse(loginUrl));
    }

    @PostMapping("/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(
            @RequestParam String code,
            @RequestParam String dest,
            HttpServletResponse response
    ) {
        OAuthUserInfo userInfo = oAuthUserInfoService.getUserInfo(code, dest);
        LoginResult result = oAuthLoginProcessor.process(userInfo);
        cookieUtil.setRefreshTokenCookie(response, result.refreshToken(), tokenProperties.refresh().expireTime());
        return ResponseEntity.ok(new LoginResponse(result.accessToken(), result.isOnboarded()));
    }
}
