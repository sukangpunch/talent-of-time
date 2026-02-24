package com.example.talentoftime.teacher.controller;

import com.example.talentoftime.teacher.dto.TeacherCreateRequest;
import com.example.talentoftime.teacher.dto.TeacherResponse;
import com.example.talentoftime.teacher.dto.TeacherUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "강사", description = "강사 관리 API")
public interface TeacherControllerDocs {

    @Operation(summary = "강사 전체 조회")
    ResponseEntity<List<TeacherResponse>> findAllTeachers();

    @Operation(summary = "강사 단건 조회")
    ResponseEntity<TeacherResponse> findTeacher(@PathVariable Long teacherId);

    @Operation(summary = "강사 등록")
    ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody TeacherCreateRequest request);

    @Operation(summary = "강사 수정")
    ResponseEntity<TeacherResponse> updateTeacher(
            @PathVariable Long teacherId,
            @Valid @RequestBody TeacherUpdateRequest request);

    @Operation(summary = "강사 삭제")
    ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId);
}
