package com.example.talentoftime.teacher.controller;

import com.example.talentoftime.teacher.dto.TeacherCreateRequest;
import com.example.talentoftime.teacher.dto.TeacherResponse;
import com.example.talentoftime.teacher.dto.TeacherSearchResponse;
import com.example.talentoftime.teacher.dto.TeacherUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "강사", description = "강사 관리 API")
public interface TeacherControllerDocs {

    @Operation(
            summary = "강사 전체 조회",
            description = "등록된 전체 강사 목록을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<TeacherResponse>> findAllTeachers();

    @Operation(
            summary = "강사 단건 조회",
            description = "강사 ID로 강사 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 강사",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "T001",
                                    summary = "T001 - 존재하지 않는 강사",
                                    value = "{\"error\": \"T001\", \"message\": \"존재하지 않는 강사입니다.\"}"
                            )
                    )
            )
    })
    ResponseEntity<TeacherResponse> findTeacher(@PathVariable Long teacherId);

    @Operation(
            summary = "강사 등록",
            description = "새 강사를 등록합니다."
    )
    @ApiResponse(responseCode = "201", description = "등록 성공")
    ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody TeacherCreateRequest request);

    @Operation(
            summary = "강사 수정",
            description = "강사 ID로 강사 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 강사",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "T001",
                                    summary = "T001 - 존재하지 않는 강사",
                                    value = "{\"error\": \"T001\", \"message\": \"존재하지 않는 강사입니다.\"}"
                            )
                    )
            )
    })
    ResponseEntity<TeacherResponse> updateTeacher(
            @PathVariable Long teacherId,
            @Valid @RequestBody TeacherUpdateRequest request);

    @Operation(
            summary = "강사 삭제",
            description = "강사 ID로 강사를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 강사",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "T001",
                                    summary = "T001 - 존재하지 않는 강사",
                                    value = "{\"error\": \"T001\", \"message\": \"존재하지 않는 강사입니다.\"}"
                            )
                    )
            )
    })
    ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId);

    @Operation(
            summary = "강사 이름 검색 (prefix 매칭)",
            description = "강사 이름의 앞부분으로 검색합니다. 정확히 일치하는 이름이 있으면 해당 강사만 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<TeacherSearchResponse>> searchTeachers(@RequestParam String name);
}
