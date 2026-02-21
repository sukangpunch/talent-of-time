package com.example.talentoftime.period.controller;

import com.example.talentoftime.period.dto.PeriodCreateRequest;
import com.example.talentoftime.period.dto.PeriodResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "교시", description = "교시 관리 API")
public interface PeriodControllerDocs {

    @Operation(
            summary = "전체 교시 조회",
            description = "등록된 전체 교시 목록을 반환합니다.\n\n"
                    + "**교시 구성**\n"
                    + "- `0교시`: 세팅 전용 (수업 시작 전)\n"
                    + "- `1교시`: 08:30 ~ 10:10\n"
                    + "- `2교시`: 10:30 ~ 12:10\n"
                    + "- `3교시`: 13:20 ~ 15:00\n"
                    + "- `4교시`: 15:20 ~ 17:00"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<PeriodResponse>> findAllPeriods();

    @Operation(
            summary = "교시 단건 조회",
            description = "교시 ID로 교시 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 교시")
    })
    ResponseEntity<PeriodResponse> findPeriod(
            @Parameter(description = "교시 ID", example = "1") Long periodId);

    @Operation(
            summary = "교시 생성",
            description = "새 교시를 등록합니다. 교시 번호가 중복된 경우 409를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "409", description = "교시 번호 중복")
    })
    ResponseEntity<PeriodResponse> createPeriod(PeriodCreateRequest request);

    @Operation(
            summary = "교시 삭제",
            description = "교시 ID로 교시를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 교시")
    })
    ResponseEntity<Void> deletePeriod(
            @Parameter(description = "교시 ID", example = "1") Long periodId);
}
