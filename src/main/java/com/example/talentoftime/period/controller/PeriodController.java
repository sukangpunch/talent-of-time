package com.example.talentoftime.period.controller;

import com.example.talentoftime.period.dto.PeriodCreateRequest;
import com.example.talentoftime.period.dto.PeriodResponse;
import com.example.talentoftime.period.service.PeriodService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/periods")
@RequiredArgsConstructor
public class PeriodController implements PeriodControllerDocs {

    private final PeriodService periodService;

    @GetMapping
    public ResponseEntity<List<PeriodResponse>> findAllPeriods() {
        return ResponseEntity.ok(periodService.findAllPeriods());
    }

    @GetMapping("/{periodId}")
    public ResponseEntity<PeriodResponse> findPeriod(
            @PathVariable Long periodId) {
        return ResponseEntity.ok(periodService.findPeriod(periodId));
    }

    @PostMapping
    public ResponseEntity<PeriodResponse> createPeriod(
            @Valid @RequestBody PeriodCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(periodService.createPeriod(request));
    }

    @DeleteMapping("/{periodId}")
    public ResponseEntity<Void> deletePeriod(
            @PathVariable Long periodId) {
        periodService.deletePeriod(periodId);
        return ResponseEntity.noContent().build();
    }
}
