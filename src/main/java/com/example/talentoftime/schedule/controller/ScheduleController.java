package com.example.talentoftime.schedule.controller;

import com.example.talentoftime.auth.domain.LoginUser;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.schedule.dto.ScheduleCreateRequest;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController implements ScheduleControllerDocs {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> selfRegister(
            @AuthenticationPrincipal LoginUser loginUser,
            @Valid @RequestBody ScheduleCreateRequest request) {
        return ResponseEntity.ok(scheduleService.selfRegister(loginUser.getId(), request));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findByClassSessionAndTaskType(
            @RequestParam Long classSessionId,
            @RequestParam TaskType taskType) {
        return ResponseEntity.ok(scheduleService.findByClassSessionAndTaskType(classSessionId, taskType));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> cancelRegistration(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable Long scheduleId) {
        scheduleService.cancelRegistration(loginUser.getId(), scheduleId);
        return ResponseEntity.noContent().build();
    }
}
