package com.example.talentoftime.crew.controller;

import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.service.CrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crews")
@RequiredArgsConstructor
public class CrewController implements CrewControllerDocs {

    private final CrewService crewService;

    @GetMapping("/{crewId}")
    public ResponseEntity<CrewResponse> findCrew(@PathVariable("crewId") Long crewId) {
        return ResponseEntity.ok(crewService.findCrew(crewId));
    }

    @GetMapping("/name")
    public ResponseEntity<CrewResponse> findCrewByName(
            @RequestParam String name) {
        return ResponseEntity.ok(crewService.findCrewByName(name));
    }
}
