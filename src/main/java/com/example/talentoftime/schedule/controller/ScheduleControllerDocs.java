package com.example.talentoftime.schedule.controller;

import com.example.talentoftime.schedule.dto.ScheduleCreateRequest;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.dto.ScheduleSwapRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "스케줄", description = "업무 스케줄 등록 및 관리 API")
public interface ScheduleControllerDocs {

    @Operation(
            summary = "스케줄 자기 등록",
            description = "인증된 크루가 교시를 선택하고 작업 내용을 골라 본인 스케줄을 직접 등록합니다.\n\n"
                    + "동일한 수업 일정(ClassSession)에 같은 작업 유형이 이미 등록되어 있으면 400을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "해당 슬롯에 동일한 작업이 이미 등록됨"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 수업 일정")
    })
    ResponseEntity<ScheduleResponse> selfRegister(Long crewId, ScheduleCreateRequest request);

    @Operation(
            summary = "스케줄 교환",
            description = "두 스케줄의 담당 크루를 서로 교환합니다.\n\n"
                    + "크루 교환 시 각 크루의 카운트도 함께 보정됩니다 "
                    + "(기존 작업 카운트 -1, 교환된 작업 카운트 +1).\n\n"
                    + "동일한 작업 유형끼리는 교환할 수 없습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "교환 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 스케줄"),
            @ApiResponse(responseCode = "409", description = "동일한 작업 유형 간 교환 불가")
    })
    ResponseEntity<Void> swapSchedules(ScheduleSwapRequest request);

    @Operation(
            summary = "스케줄 등록 취소",
            description = "인증된 크루가 본인이 등록한 스케줄을 취소합니다.\n\n"
                    + "본인 스케줄이 아닌 경우 403을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "취소 성공"),
            @ApiResponse(responseCode = "403", description = "본인 스케줄이 아님"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 스케줄")
    })
    ResponseEntity<Void> cancelRegistration(
            Long crewId,
            @Parameter(description = "스케줄 ID", example = "1") Long scheduleId);
}
