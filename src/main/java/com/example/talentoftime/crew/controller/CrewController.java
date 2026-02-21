package com.example.talentoftime.crew.controller;

import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.crew.dto.CrewCreateRequest;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.CrewUpdateRequest;
import com.example.talentoftime.crew.service.CrewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crews")
@RequiredArgsConstructor
public class CrewController implements CrewControllerDocs {

    private final CrewService crewService;

    @GetMapping
    public ResponseEntity<List<CrewResponse>> findCrews(
            @RequestParam(required = false) CrewType crewType) {
        if (crewType != null) {
            return ResponseEntity.ok(crewService.findCrewsByType(crewType));
        }
        return ResponseEntity.ok(crewService.findAllCrews());
    }

    @GetMapping("/{crewId}")
    public ResponseEntity<CrewResponse> findCrew(
            @PathVariable Long crewId) {
        return ResponseEntity.ok(crewService.findCrew(crewId));
    }

    @PostMapping
    public ResponseEntity<CrewResponse> createCrew(
            @Valid @RequestBody CrewCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crewService.createCrew(request));
    }

    @PatchMapping("/{crewId}")
    public ResponseEntity<CrewResponse> updateCrew(
            @PathVariable Long crewId,
            @Valid @RequestBody CrewUpdateRequest request) {
        return ResponseEntity.ok(crewService.updateCrew(crewId, request));
    }

    @DeleteMapping("/{crewId}")
    public ResponseEntity<Void> deleteCrew(
            @PathVariable Long crewId) {
        crewService.deleteCrew(crewId);
        return ResponseEntity.noContent().build();
    }
}
