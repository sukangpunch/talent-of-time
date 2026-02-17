package com.example.talentoftime.classroom.controller;

import com.example.talentoftime.classroom.dto.ClassroomCreateRequest;
import com.example.talentoftime.classroom.dto.ClassroomResponse;
import com.example.talentoftime.classroom.service.ClassroomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<List<ClassroomResponse>> findAllClassrooms() {
        List<ClassroomResponse> response = classroomService.findAllClassrooms();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{classroomId}")
    public ResponseEntity<ClassroomResponse> findClassroom(
            @PathVariable Long classroomId) {
        ClassroomResponse response = classroomService.findClassroom(classroomId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ClassroomResponse> createClassroom(
            @Validated @RequestBody ClassroomCreateRequest request) {
        ClassroomResponse response = classroomService.createClassroom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{classroomId}")
    public ResponseEntity<Void> deleteClassroom(
            @PathVariable Long classroomId) {
        classroomService.deleteClassroom(classroomId);
        return ResponseEntity.noContent().build();
    }
}
