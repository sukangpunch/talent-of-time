package com.example.talentoftime.schedule.controller;

import com.example.talentoftime.auth.domain.LoginUser;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.schedule.dto.ScheduleCreateRequest;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "스케줄", description = "업무 스케줄 등록 및 관리 API")
public interface ScheduleControllerDocs {

    @Operation(
            summary = "작업별 등록 크루 조회",
            description = "특정 수업 일정(ClassSession)과 작업 유형에 등록된 크루 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 수업 일정",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "CS001",
                                    summary = "CS001 - 존재하지 않는 수업 일정",
                                    value = "{\"error\": \"CS001\", \"message\": \"존재하지 않는 수업 일정입니다.\"}"
                            )
                    )
            )
    })
    ResponseEntity<List<ScheduleResponse>> findByClassSessionAndTaskType(
            @Parameter(description = "수업 일정 ID", example = "1") Long classSessionId,
            @Parameter(description = "작업 유형", example = "SETTING") TaskType taskType);

    @Operation(
            summary = "스케줄 자기 등록",
            description = "인증된 크루가 교시를 선택하고 작업 내용을 골라 본인 스케줄을 직접 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 필요",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "A002",
                                    summary = "A002 - 토큰 없음 또는 유효하지 않은 토큰",
                                    value = "{\"error\": \"A002\", \"message\": \"인증이 필요합니다.\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 리소스",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "C001",
                                            summary = "C001 - 존재하지 않는 크루",
                                            value = "{\"error\": \"C001\", \"message\": \"존재하지 않는 크루입니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "CS001",
                                            summary = "CS001 - 존재하지 않는 수업 일정",
                                            value = "{\"error\": \"CS001\", \"message\": \"존재하지 않는 수업 일정입니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "CT001",
                                            summary = "CT001 - count 정보 없음 (내부 데이터 오류)",
                                            value = "{\"error\": \"CT001\", \"message\": \"count 정보를 찾을 수 없습니다.\"}"
                                    )
                            }
                    )
            )
    })
    ResponseEntity<ScheduleResponse> selfRegister(LoginUser loginUser, ScheduleCreateRequest request);

    @Operation(
            summary = "스케줄 등록 취소",
            description = "인증된 크루가 본인이 등록한 스케줄을 취소합니다.\n\n"
                    + "본인 스케줄이 아닌 경우 403을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "취소 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 필요",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "A002",
                                    summary = "A002 - 토큰 없음 또는 유효하지 않은 토큰",
                                    value = "{\"error\": \"A002\", \"message\": \"인증이 필요합니다.\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "본인 스케줄이 아님",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "S006",
                                    summary = "S006 - 다른 크루의 스케줄 취소 시도",
                                    value = "{\"error\": \"S006\", \"message\": \"본인의 스케줄만 삭제할 수 있습니다.\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 스케줄",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "S001",
                                    summary = "S001 - 존재하지 않는 스케줄",
                                    value = "{\"error\": \"S001\", \"message\": \"존재하지 않는 스케줄입니다.\"}"
                            )
                    )
            )
    })
    ResponseEntity<Void> cancelRegistration(
            LoginUser loginUser,
            @Parameter(description = "스케줄 ID", example = "1") Long scheduleId);
}
