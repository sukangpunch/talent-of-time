package com.example.talentoftime.crew.controller;

import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.crew.dto.CrewCreateRequest;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.CrewUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "크루", description = "크루(알바생) 관리 API")
public interface CrewControllerDocs {

    @Operation(
            summary = "크루 목록 조회",
            description = "전체 크루 목록을 반환합니다.\n\n"
                    + "`crewType` 파라미터를 전달하면 해당 유형만 필터링합니다.\n\n"
                    + "**크루 유형**\n"
                    + "- `MORNING`: 오전 크루 (수업 전부터 근무)\n"
                    + "- `MIDDLE`: 미들 크루 (오전 9시 ~ 오후 5시)\n"
                    + "- `AFTERNOON`: 오후 크루 (오후 2시 20분 ~ 오후 10시)"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<CrewResponse>> findCrews(
            @Parameter(description = "크루 유형 필터 (MORNING / MIDDLE / AFTERNOON)", example = "MORNING") CrewType crewType);

    @Operation(
            summary = "크루 단건 조회",
            description = "크루 ID로 크루 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 크루")
    })
    ResponseEntity<CrewResponse> findCrew(
            @Parameter(description = "크루 ID", example = "1") Long crewId);

    @Operation(
            summary = "크루 생성",
            description = "새 크루를 등록합니다. 크루 생성 시 모든 작업 유형의 카운트가 0으로 자동 초기화됩니다.\n\n"
                    + "이름이 중복된 경우 409를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "409", description = "이름 중복")
    })
    ResponseEntity<CrewResponse> createCrew(CrewCreateRequest request);

    @Operation(
            summary = "크루 정보 수정",
            description = "크루의 이름 또는 유형을 수정합니다. 수정하지 않을 필드는 null로 전달하세요."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 크루")
    })
    ResponseEntity<CrewResponse> updateCrew(
            @Parameter(description = "크루 ID", example = "1") Long crewId,
            CrewUpdateRequest request);

    @Operation(
            summary = "크루 삭제",
            description = "크루 ID로 크루를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 크루")
    })
    ResponseEntity<Void> deleteCrew(
            @Parameter(description = "크루 ID", example = "1") Long crewId);
}
