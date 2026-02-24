package com.example.talentoftime.schedule.controller;

import com.example.talentoftime.schedule.dto.ScheduleCreateRequest;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.dto.ScheduleSwapRequest;
import com.example.talentoftime.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController implements ScheduleControllerDocs {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> selfRegister(
            @AuthenticationPrincipal Long crewId,
            @Valid @RequestBody ScheduleCreateRequest request) {
        return ResponseEntity.ok(scheduleService.selfRegister(crewId, request));
    }

    @PostMapping("/swap")
    public ResponseEntity<Void> swapSchedules(
            @Valid @RequestBody ScheduleSwapRequest request) {
        scheduleService.swapSchedules(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> cancelRegistration(
            @AuthenticationPrincipal Long crewId,
            @PathVariable Long scheduleId) {
        scheduleService.cancelRegistration(crewId, scheduleId);
        return ResponseEntity.noContent().build();
    }
}
