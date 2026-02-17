package com.example.talentoftime.schedule.controller;

import com.example.talentoftime.schedule.dto.ScheduleAutoAssignRequest;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.dto.ScheduleSwapRequest;
import com.example.talentoftime.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getSchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "period-number", required = false) Integer periodNumber) {
        if (periodNumber == null) {
            return ResponseEntity.ok(scheduleService.findSchedulesByDate(date));
        }
        return ResponseEntity.ok(scheduleService.findSchedulesByDateAndPeriod(date, periodNumber));
    }

    @PostMapping("/setting/auto-assign")
    public ResponseEntity<List<ScheduleResponse>> assignSettingSchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ScheduleResponse> responses = scheduleService.assignSettingSchedules(date);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PostMapping("/period/auto-assign")
    public ResponseEntity<List<ScheduleResponse>> assignPeriodSchedules(
            @Valid @RequestBody ScheduleAutoAssignRequest request) {
        List<ScheduleResponse> responses = scheduleService.assignPeriodSchedules(
                request.getDate(),
                request.getPeriodNumber(),
                request.getClassroomId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PostMapping("/swap")
    public ResponseEntity<Void> swapSchedules(
            @Valid @RequestBody ScheduleSwapRequest request) {
        scheduleService.swapSchedules(request);
        return ResponseEntity.noContent().build();
    }
}
