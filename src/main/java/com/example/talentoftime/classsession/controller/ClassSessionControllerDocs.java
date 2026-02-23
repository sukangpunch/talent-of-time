package com.example.talentoftime.classsession.controller;

import com.example.talentoftime.classsession.dto.ClassSessionBulkCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionResponse;
import com.example.talentoftime.classsession.dto.ClassSessionUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "수업 일정", description = "주간 수업 일정(ClassSession) 관리 API")
public interface ClassSessionControllerDocs {

    @Operation(
            summary = "수업 일정 단건 조회",
            description = "수업 일정 ID로 단건 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 수업 일정")
    })
    ResponseEntity<ClassSessionResponse> findClassSession(
            @Parameter(description = "수업 일정 ID", example = "1") Long classSessionId);

    @Operation(
            summary = "날짜별 수업 일정 조회",
            description = "특정 날짜의 수업 일정 목록을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<ClassSessionResponse>> findClassSessionsByDate(
            @Parameter(description = "조회 날짜 (yyyy-MM-dd)", example = "2026-02-23") LocalDate date);

    @Operation(
            summary = "오늘 수업 일정 조회",
            description = "오늘 날짜의 수업 일정 목록을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<ClassSessionResponse>> findTodayClassSessions();

    @Operation(
            summary = "주간 수업 일정 조회",
            description = "date 파라미터가 속한 주(월~일)의 전체 수업 일정을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<ClassSessionResponse>> findWeeklyClassSessions(
            @Parameter(description = "기준 날짜 (yyyy-MM-dd)", example = "2026-02-23") LocalDate date);

    @Operation(
            summary = "수업 일정 단건 등록",
            description = "날짜 + 교시 + 강의실 조합으로 수업 일정을 등록합니다.\n\n"
                    + "동일한 조합이 이미 존재하면 400을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "중복된 수업 일정"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 교시 또는 강의실")
    })
    ResponseEntity<ClassSessionResponse> createClassSession(ClassSessionCreateRequest request);

    @Operation(
            summary = "수업 일정 일괄 등록",
            description = "여러 수업 일정을 한 번에 등록합니다. 하나라도 중복이 있으면 전체 롤백됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "중복된 수업 일정 포함"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 교시 또는 강의실")
    })
    ResponseEntity<List<ClassSessionResponse>> createBulkClassSessions(ClassSessionBulkCreateRequest request);

    @Operation(
            summary = "수업 일정 수정",
            description = "수업 일정의 날짜·교시·강의실을 수정합니다.\n\n"
                    + "연결된 크루 배정(Schedule)이 있는 경우, 해당 배정은 삭제되고 count도 복구됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "수정 결과가 기존 다른 일정과 중복"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 수업 일정")
    })
    ResponseEntity<ClassSessionResponse> updateClassSession(
            @Parameter(description = "수업 일정 ID", example = "1") Long classSessionId,
            ClassSessionUpdateRequest request);

    @Operation(
            summary = "수업 일정 삭제",
            description = "수업 일정을 삭제합니다.\n\n"
                    + "연결된 크루 배정(Schedule)이 있는 경우, 해당 배정도 함께 삭제되고 count도 복구됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 수업 일정")
    })
    ResponseEntity<Void> deleteClassSession(
            @Parameter(description = "수업 일정 ID", example = "1") Long classSessionId);
}
