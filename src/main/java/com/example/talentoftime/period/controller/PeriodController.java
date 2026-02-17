package com.example.talentoftime.period.controller;

import com.example.talentoftime.period.dto.PeriodCreateRequest;
import com.example.talentoftime.period.dto.PeriodResponse;
import com.example.talentoftime.period.service.PeriodService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/periods")
@RequiredArgsConstructor
public class PeriodController {

    private final PeriodService periodService;

    @GetMapping
    public ResponseEntity<List<PeriodResponse>> findAllPeriods() {
        List<PeriodResponse> response = periodService.findAllPeriods();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{periodId}")
    public ResponseEntity<PeriodResponse> findPeriod(
            @PathVariable Long periodId) {
        PeriodResponse response = periodService.findPeriod(periodId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PeriodResponse> createPeriod(
            @RequestBody @Validated PeriodCreateRequest request) {
        PeriodResponse response = periodService.createPeriod(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{periodId}")
    public ResponseEntity<Void> deletePeriod(
            @PathVariable Long periodId) {
        periodService.deletePeriod(periodId);
        return ResponseEntity.noContent().build();
    }
}
