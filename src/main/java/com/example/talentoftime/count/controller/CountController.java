package com.example.talentoftime.count.controller;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.count.dto.CountResponse;
import com.example.talentoftime.count.service.CountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/counts")
@RequiredArgsConstructor
public class CountController implements CountControllerDocs {

    private final CountService countService;

    @GetMapping
    public ResponseEntity<List<CountResponse>> findAllCounts() {
        return ResponseEntity.ok(countService.findAllCounts());
    }

    @GetMapping("/crew/{crewId}")
    public ResponseEntity<List<CountResponse>> findCountsByCrew(
            @PathVariable Long crewId) {
        return ResponseEntity.ok(countService.findCountsByCrew(crewId));
    }

    @GetMapping("/task-type/{taskType}")
    public ResponseEntity<List<CountResponse>> findCountsByTaskType(
            @PathVariable TaskType taskType) {
        return ResponseEntity.ok(countService.findCountsByTaskType(taskType));
    }
}
