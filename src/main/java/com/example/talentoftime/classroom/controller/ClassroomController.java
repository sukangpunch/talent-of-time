package com.example.talentoftime.classroom.controller;

import com.example.talentoftime.classroom.dto.ClassroomCreateRequest;
import com.example.talentoftime.classroom.dto.ClassroomResponse;
import com.example.talentoftime.classroom.service.ClassroomService;
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
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassroomController implements ClassroomControllerDocs {

    private final ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<List<ClassroomResponse>> findAllClassrooms() {
        return ResponseEntity.ok(classroomService.findAllClassrooms());
    }

    @GetMapping("/{classroomId}")
    public ResponseEntity<ClassroomResponse> findClassroom(
            @PathVariable Long classroomId) {
        return ResponseEntity.ok(classroomService.findClassroom(classroomId));
    }

    @PostMapping
    public ResponseEntity<ClassroomResponse> createClassroom(
            @Valid @RequestBody ClassroomCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(classroomService.createClassroom(request));
    }

    @DeleteMapping("/{classroomId}")
    public ResponseEntity<Void> deleteClassroom(
            @PathVariable Long classroomId) {
        classroomService.deleteClassroom(classroomId);
        return ResponseEntity.noContent().build();
    }
}
