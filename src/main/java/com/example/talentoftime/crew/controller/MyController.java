package com.example.talentoftime.crew.controller;

import com.example.talentoftime.auth.domain.LoginUser;
import com.example.talentoftime.count.dto.MyCountResponse;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.OnboardingRequest;
import com.example.talentoftime.crew.service.MyService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyController implements MyControllerDocs {

    private final MyService myService;

    @GetMapping("/profile")
    public ResponseEntity<CrewResponse> getProfile(@AuthenticationPrincipal Long crewId) {
        return ResponseEntity.ok(myService.getProfile(crewId));
    }

    @GetMapping("/counts")
    public ResponseEntity<List<MyCountResponse>> getMyCounts(@AuthenticationPrincipal Long crewId) {
        return ResponseEntity.ok(myService.getMyCounts(crewId));
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
