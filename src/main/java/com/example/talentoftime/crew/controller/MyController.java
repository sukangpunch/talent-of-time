package com.example.talentoftime.crew.controller;

import com.example.talentoftime.auth.domain.LoginUser;
import com.example.talentoftime.count.dto.MyCountResponse;
import com.example.talentoftime.crew.dto.CrewHeaderResponse;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.FcmTokenRequest;
import com.example.talentoftime.crew.dto.FcmTokenResponse;
import com.example.talentoftime.crew.dto.OnboardingRequest;
import com.example.talentoftime.crew.service.MyService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/my")
@RequiredArgsConstructor
@Slf4j
public class MyController implements MyControllerDocs {

    private final MyService myService;

    @PatchMapping("/fcm-token")
    public ResponseEntity<Void> saveFcmToken(
            @AuthenticationPrincipal LoginUser loginUser,
            @Valid @RequestBody FcmTokenRequest request
    ) {
        log.info("[saveFcmToken] userId: {}, fcmToekn : {}", loginUser.getId(), request.fcmToken());
        myService.saveFcmToken(loginUser.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/fcm-token")
    public ResponseEntity<List<FcmTokenResponse>> getFcmTokens(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(myService.getFcmTokens(loginUser.getId()));
    }

    @GetMapping("/header")
    public ResponseEntity<CrewHeaderResponse> getHeader(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(myService.getHeader(loginUser.getId()));
    }

    @GetMapping("/profile")
    public ResponseEntity<CrewResponse> getProfile(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(myService.getProfile(loginUser.getId()));
    }

    @GetMapping("/counts")
    public ResponseEntity<List<MyCountResponse>> getMyCounts(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(myService.getMyCounts(loginUser.getId()));
    }

    @PostMapping("/onboarding")
    public ResponseEntity<CrewResponse> onboardUser(
            @AuthenticationPrincipal LoginUser loginUser,
            @Valid @RequestBody OnboardingRequest request
    ) {
        CrewResponse crewResponse = myService.onboarding(loginUser.getId(), request);
        return ResponseEntity.ok(crewResponse);
    }
}
