package com.example.talentoftime.classsession.controller;

import com.example.talentoftime.classsession.dto.ClassSessionBulkCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionResponse;
import com.example.talentoftime.classsession.dto.ClassSessionUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "수업 일정", description = "주간 수업 일정(ClassSession) 관리 API")
public interface ClassSessionControllerDocs {

    @Operation(
            summary = "당일 수업 일정 조회",
            description = "오늘 날짜의 수업 일정 목록을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<ClassSessionResponse>> findTodayClassSessions();

    @Operation(
            summary = "날짜별 수업 일정 조회",
            description = "특정 날짜의 수업 일정 목록을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<ClassSessionResponse>> findClassSessionsByDate(
            @Parameter(description = "조회 날짜 (yyyy-MM-dd)", example = "2026-02-24") LocalDate date);

    @Operation(
            summary = "저번 주 같은 요일 수업 일정 조회",
            description = "입력한 날짜의 저번 주 같은 요일(7일 전) 수업 일정 목록을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<ClassSessionResponse>> findLastWeekSameDayClassSessions(
            @Parameter(description = "기준 날짜 (yyyy-MM-dd)", example = "2026-03-02") LocalDate date);

    @Operation(
            summary = "수업 일정 일괄 등록 (주간)",
            description = "한 주치 수업 일정을 한 번에 등록합니다. 하나라도 중복이 있으면 전체 롤백됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 요청 파라미터",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "G001",
                                    summary = "G001 - sessions 목록이 비어있음",
                                    value = "{\"error\": \"G001\", \"message\": \"유효하지 않은 요청 파라미터입니다.\"}"
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
                                            name = "P001",
                                            summary = "P001 - 존재하지 않는 교시",
                                            value = "{\"error\": \"P001\", \"message\": \"존재하지 않는 교시입니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "R001",
                                            summary = "R001 - 존재하지 않는 강의실",
                                            value = "{\"error\": \"R001\", \"message\": \"존재하지 않는 강의실입니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "T001",
                                            summary = "T001 - 존재하지 않는 강사 (teacherId 제공 시)",
                                            value = "{\"error\": \"T001\", \"message\": \"존재하지 않는 강사입니다.\"}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "수업 일정 중복",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "CS002",
                                    summary = "CS002 - 요청 내 또는 DB에 이미 존재하는 수업 일정",
                                    value = "{\"error\": \"CS002\", \"message\": \"수업 일정이 중복됩니다.\"}"
                            )
                    )
            )
    })
    ResponseEntity<List<ClassSessionResponse>> createBulkClassSessions(ClassSessionBulkCreateRequest request);

    @Operation(
            summary = "수업 일정 수정",
            description = "수업 일정의 교시·강의실을 수정합니다. (PATCH — null 필드는 기존 값 유지)\n\n"
                    + "연결된 크루 배정(Schedule)이 있는 경우, 해당 배정은 삭제되고 count도 복구됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 리소스",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "CS001",
                                            summary = "CS001 - 존재하지 않는 수업 일정",
                                            value = "{\"error\": \"CS001\", \"message\": \"존재하지 않는 수업 일정입니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "P001",
                                            summary = "P001 - 존재하지 않는 교시",
                                            value = "{\"error\": \"P001\", \"message\": \"존재하지 않는 교시입니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "R001",
                                            summary = "R001 - 존재하지 않는 강의실",
                                            value = "{\"error\": \"R001\", \"message\": \"존재하지 않는 강의실입니다.\"}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "수업 일정 중복",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "CS002",
                                    summary = "CS002 - 수정 결과가 다른 일정과 중복",
                                    value = "{\"error\": \"CS002\", \"message\": \"수업 일정이 중복됩니다.\"}"
                            )
                    )
            )
    })
    ResponseEntity<ClassSessionResponse> updateClassSession(
            @Parameter(description = "수업 일정 ID", example = "1") Long classSessionId,
            ClassSessionUpdateRequest request);

    @Operation(
            summary = "수업 일정 휴강 처리",
            description = "수업 일정을 휴강으로 처리합니다.\n\n"
                    + "연결된 크루 배정(Schedule)이 있는 경우, 해당 배정도 함께 삭제되고 count도 복구됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "휴강 처리 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 휴강 처리된 수업 일정",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "CS004",
                                    summary = "CS004 - 이미 휴강 처리된 수업 일정",
                                    value = "{\"error\": \"CS004\", \"message\": \"이미 휴강 처리된 수업 일정입니다.\"}"
                            )
                    )
            ),
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
    ResponseEntity<ClassSessionResponse> cancelClassSession(
            @Parameter(description = "수업 일정 ID", example = "1") Long classSessionId);
}
