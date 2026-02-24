package com.example.talentoftime.auth.controller;

import com.example.talentoftime.auth.dto.LoginResponse;
import com.example.talentoftime.auth.dto.LoginUrlResponse;
import com.example.talentoftime.auth.dto.oauth.OAuthUserInfo;
import com.example.talentoftime.auth.service.oauth.OAuthLoginProcessor;
import com.example.talentoftime.auth.service.oauth.OAuthLoginUrlService;
import com.example.talentoftime.auth.service.oauth.OAuthUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/login-url")
    public ResponseEntity<LoginUrlResponse> authorizeUrl(
            @RequestParam String dest
    ) {
        String loginUrl = oAuthLoginUrlService.generateLoginUrl(dest);
        return ResponseEntity.ok(new LoginUrlResponse(loginUrl));
    }

    @GetMapping("/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(
            @RequestParam String code,
            @RequestParam String dest
    ) {
        OAuthUserInfo userInfo = oAuthUserInfoService.getUserInfo(code, dest);
        LoginResponse response = oAuthLoginProcessor.process(userInfo);
        return ResponseEntity.ok(response);
    }
}
