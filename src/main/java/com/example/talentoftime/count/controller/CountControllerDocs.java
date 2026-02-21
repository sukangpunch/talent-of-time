package com.example.talentoftime.count.controller;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.count.dto.CountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "카운트", description = "크루 작업 횟수 관리 API")
public interface CountControllerDocs {

    @Operation(
            summary = "전체 카운트 조회",
            description = "전체 크루의 모든 작업 유형별 수행 횟수를 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<CountResponse>> findAllCounts();

    @Operation(
            summary = "크루별 카운트 조회",
            description = "특정 크루의 모든 작업 유형별 수행 횟수를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 크루")
    })
    ResponseEntity<List<CountResponse>> findCountsByCrew(
            @Parameter(description = "크루 ID", example = "1") Long crewId);

    @Operation(
            summary = "작업 유형별 카운트 조회",
            description = "특정 작업 유형을 수행한 크루 목록과 횟수를 반환합니다.\n\n"
                    + "**작업 유형 목록**\n"
                    + "- `SETTING`: 수업 전 강의실 세팅\n"
                    + "- `ENTRY`: 입실 안내 및 자료 배부\n"
                    + "- `JOG`: 수업 녹화 및 인코딩\n"
                    + "- `EXIT`: 퇴실 및 강의실 정리\n"
                    + "- `EXAM_SUPERVISION`: 시험 감독"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 작업 유형")
    })
    ResponseEntity<List<CountResponse>> findCountsByTaskType(
            @Parameter(description = "작업 유형 (SETTING / ENTRY / JOG / EXIT / EXAM_SUPERVISION)", example = "ENTRY") TaskType taskType);

    @Operation(
            summary = "크루 카운트 초기화",
            description = "특정 크루의 모든 작업 유형 수행 횟수를 0으로 초기화합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "초기화 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 크루")
    })
    ResponseEntity<Void> resetCountsByCrew(
            @Parameter(description = "크루 ID", example = "1") Long crewId);

    @Operation(
            summary = "전체 카운트 초기화",
            description = "전체 크루의 모든 작업 수행 횟수를 0으로 초기화합니다."
    )
    @ApiResponse(responseCode = "204", description = "초기화 성공")
    ResponseEntity<Void> resetAllCounts();
}
