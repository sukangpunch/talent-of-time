package com.example.talentoftime.classroom.controller;

import com.example.talentoftime.classroom.dto.ClassroomCreateRequest;
import com.example.talentoftime.classroom.dto.ClassroomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "강의실", description = "강의실 관리 API")
public interface ClassroomControllerDocs {

    @Operation(
            summary = "전체 강의실 조회",
            description = "등록된 전체 강의실 목록을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<ClassroomResponse>> findAllClassrooms();

    @Operation(
            summary = "강의실 단건 조회",
            description = "강의실 ID로 단건 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 강의실")
    })
    ResponseEntity<ClassroomResponse> findClassroom(
            @Parameter(description = "강의실 ID", example = "1") Long classroomId);

    @Operation(
            summary = "강의실 생성",
            description = "새 강의실을 등록합니다. 이미 등록된 호수가 중복되면 409를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "409", description = "호수 중복")
    })
    ResponseEntity<ClassroomResponse> createClassroom(ClassroomCreateRequest request);

    @Operation(
            summary = "강의실 삭제",
            description = "강의실 ID로 강의실을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 강의실")
    })
    ResponseEntity<Void> deleteClassroom(
            @Parameter(description = "강의실 ID", example = "1") Long classroomId);
}
