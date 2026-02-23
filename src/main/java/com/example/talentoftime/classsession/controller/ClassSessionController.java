package com.example.talentoftime.classsession.controller;

import com.example.talentoftime.classsession.dto.ClassSessionBulkCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionResponse;
import com.example.talentoftime.classsession.dto.ClassSessionUpdateRequest;
import com.example.talentoftime.classsession.service.ClassSessionService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/class-sessions")
@RequiredArgsConstructor
public class ClassSessionController implements ClassSessionControllerDocs {

    private final ClassSessionService classSessionService;

    @GetMapping("/{classSessionId}")
    public ResponseEntity<ClassSessionResponse> findClassSession(
            @PathVariable Long classSessionId) {
        return ResponseEntity.ok(classSessionService.findClassSession(classSessionId));
    }

    @GetMapping
    public ResponseEntity<List<ClassSessionResponse>> findClassSessionsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(classSessionService.findClassSessionsByDate(date));
    }

    @GetMapping("/today")
    public ResponseEntity<List<ClassSessionResponse>> findTodayClassSessions() {
        return ResponseEntity.ok(classSessionService.findTodayClassSessions());
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<ClassSessionResponse>> findWeeklyClassSessions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(classSessionService.findWeeklyClassSessions(date));
    }

    @PostMapping
    public ResponseEntity<ClassSessionResponse> createClassSession(
            @Valid @RequestBody ClassSessionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(classSessionService.createClassSession(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ClassSessionResponse>> createBulkClassSessions(
            @Valid @RequestBody ClassSessionBulkCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(classSessionService.createBulkClassSessions(request));
    }

    @PutMapping("/{classSessionId}")
    public ResponseEntity<ClassSessionResponse> updateClassSession(
            @PathVariable Long classSessionId,
            @Valid @RequestBody ClassSessionUpdateRequest request) {
        return ResponseEntity.ok(classSessionService.updateClassSession(classSessionId, request));
    }

    @DeleteMapping("/{classSessionId}")
    public ResponseEntity<Void> deleteClassSession(
            @PathVariable Long classSessionId) {
        classSessionService.deleteClassSession(classSessionId);
        return ResponseEntity.noContent().build();
    }
}
