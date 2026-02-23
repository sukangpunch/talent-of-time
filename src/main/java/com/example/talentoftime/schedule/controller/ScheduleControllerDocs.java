package com.example.talentoftime.schedule.controller;

import com.example.talentoftime.schedule.dto.ScheduleAutoAssignRequest;
import com.example.talentoftime.schedule.dto.ScheduleCreateRequest;
import com.example.talentoftime.schedule.dto.ScheduleResponse;
import com.example.talentoftime.schedule.dto.ScheduleSwapRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "스케줄", description = "업무 스케줄 자동 배정 및 조회 API")
public interface ScheduleControllerDocs {

    @Operation(
            summary = "날짜별 스케줄 조회",
            description = "특정 날짜의 전체 스케줄 목록을 반환합니다.\n\n"
                    + "`periodNumber`를 함께 전달하면 해당 교시의 스케줄만 필터링합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 교시")
    })
    ResponseEntity<List<ScheduleResponse>> findSchedules(
            @Parameter(description = "조회 날짜 (yyyy-MM-dd)", example = "2026-02-19") LocalDate date,
            @Parameter(description = "교시 번호 (0~4). 생략 시 전체 교시 반환", example = "1") Integer periodNumber);

    @Operation(summary = "오늘 스케줄 조회")
    ResponseEntity<List<ScheduleResponse>> findTodaySchedules();

    @Operation(
            summary = "하루 전체 스케줄 자동 배정",
            description = "등록된 수업 일정(ClassSession)을 기반으로 해당 날짜의 전체 스케줄을 한 번에 자동 배정합니다.\n\n"
                    + "**배정 순서:**\n"
                    + "1. 세팅: 해당 날짜에 수업이 있는 강의실에 오전 크루 배정\n"
                    + "2. 각 수업 일정(ClassSession)마다 입실·조그·퇴실 크루 배정\n\n"
                    + "**교시별 배정 가능 크루:**\n"
                    + "- `1교시` 입실·조그: 오전, 퇴실: 오전·미들\n"
                    + "- `2교시`: 오전·미들\n"
                    + "- `3교시` 입실·조그: 오전·미들, 퇴실: 오후\n"
                    + "- `4교시`: 미들·오후\n"
                    + "- `5·6교시`: 오후\n\n"
                    + "해당 날짜에 수업 일정이 없거나 이미 배정된 경우 에러를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배정 성공"),
            @ApiResponse(responseCode = "404", description = "수업 일정 없음 또는 0교시 미등록"),
            @ApiResponse(responseCode = "409", description = "이미 배정된 스케줄")
    })
    ResponseEntity<List<ScheduleResponse>> assignDailySchedules(LocalDate date);

    @Operation(
            summary = "세팅 스케줄 자동 배정",
            description = "수업 시작 전 강의실 세팅 작업을 오전 크루에게 자동으로 배정합니다.\n\n"
                    + "카운트(SETTING 수행 횟수)가 가장 낮은 크루에게 우선 배정되며, "
                    + "동일 카운트인 경우 랜덤으로 선택됩니다.\n\n"
                    + "등록된 전체 강의실 수만큼 스케줄이 생성됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배정 성공"),
            @ApiResponse(responseCode = "404", description = "0교시 미등록 또는 배정 가능한 크루 없음")
    })
    ResponseEntity<List<ScheduleResponse>> assignSettingSchedules(
            @Parameter(description = "배정 날짜 (yyyy-MM-dd)", example = "2026-02-19") LocalDate date);

    @Operation(
            summary = "교시 스케줄 자동 배정",
            description = "특정 교시·강의실의 입실·조그·퇴실 작업을 크루에게 자동 배정합니다.\n\n"
                    + "교시별 배정 가능한 크루 유형 규칙:\n"
                    + "- `1교시` 입실·조그: 오전 크루만 배정 가능\n"
                    + "- `1교시` 퇴실: 오전·미들 크루 배정 가능\n"
                    + "- `2교시`: 오전·미들 크루 배정 가능\n"
                    + "- `3교시` 입실·조그: 오전·미들 크루 배정 가능\n"
                    + "- `3교시` 퇴실: 오후 크루만 배정 가능\n"
                    + "- `4교시`: 미들·오후 크루 배정 가능\n\n"
                    + "이미 배정된 교시·강의실 조합은 409를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 교시 또는 강의실"),
            @ApiResponse(responseCode = "409", description = "이미 배정된 스케줄")
    })
    ResponseEntity<List<ScheduleResponse>> assignPeriodSchedules(ScheduleAutoAssignRequest request);

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

    @Operation(summary = "스케줄 자기 등록", description = "인증된 크루가 본인 스케줄을 직접 등록합니다.")
    ResponseEntity<ScheduleResponse> selfRegister(Long crewId, ScheduleCreateRequest request);

    @Operation(summary = "스케줄 등록 취소", description = "인증된 크루가 본인이 등록한 스케줄을 취소합니다.")
    ResponseEntity<Void> cancelRegistration(Long crewId, Long scheduleId);
}
