package com.example.talentoftime.crew.controller;

import com.example.talentoftime.count.dto.MyCountResponse;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.service.MyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @GetMapping("/profile")
    public ResponseEntity<CrewResponse> getProfile(@AuthenticationPrincipal Long crewId) {
        return ResponseEntity.ok(myService.getProfile(crewId));
    }

    @GetMapping("/counts")
    public ResponseEntity<List<MyCountResponse>> getMyCounts(@AuthenticationPrincipal Long crewId) {
        return ResponseEntity.ok(myService.getMyCounts(crewId));
    }
}
