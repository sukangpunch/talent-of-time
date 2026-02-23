package com.example.talentoftime.schedule.controller;

import com.example.talentoftime.schedule.dto.ScheduleAutoAssignRequest;
import com.example.talentoftime.schedule.dto.ScheduleCreateRequest;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.dto.ScheduleSwapRequest;
import com.example.talentoftime.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findSchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer periodNumber) {
        if (periodNumber != null) {
            return ResponseEntity.ok(scheduleService.findSchedulesByDateAndPeriod(date, periodNumber));
        }
        return ResponseEntity.ok(scheduleService.findSchedulesByDate(date));
    }

    @GetMapping("/today")
    public ResponseEntity<List<ScheduleResponse>> findTodaySchedules() {
        return ResponseEntity.ok(scheduleService.findSchedulesByDate(LocalDate.now()));
    }

    @PostMapping("/daily-assign")
    public ResponseEntity<List<ScheduleResponse>> assignDailySchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.assignDailySchedules(date));
    }

    @PostMapping("/setting")
    public ResponseEntity<List<ScheduleResponse>> assignSettingSchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.assignSettingSchedules(date));
    }

    @PostMapping("/period")
    public ResponseEntity<List<ScheduleResponse>> assignPeriodSchedules(
            @Valid @RequestBody ScheduleAutoAssignRequest request) {
        return ResponseEntity.ok(scheduleService.assignPeriodSchedules(
                request.getDate(),
                request.getPeriodNumber(),
                request.getClassroomId()
        ));
    }

    @PostMapping("/swap")
    public ResponseEntity<Void> swapSchedules(
            @Valid @RequestBody ScheduleSwapRequest request) {
        scheduleService.swapSchedules(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ScheduleResponse> selfRegister(
            @AuthenticationPrincipal Long crewId,
            @Valid @RequestBody ScheduleCreateRequest request) {
        return ResponseEntity.ok(scheduleService.selfRegister(crewId, request));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> cancelRegistration(
            @AuthenticationPrincipal Long crewId,
            @PathVariable Long scheduleId) {
        scheduleService.cancelRegistration(crewId, scheduleId);
        return ResponseEntity.noContent().build();
    }
}
