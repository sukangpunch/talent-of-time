package com.example.talentoftime.crew.controller;

import com.example.talentoftime.crew.dto.CrewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "크루", description = "크루(알바생) 조회 API")
public interface CrewControllerDocs {

    @Operation(
            summary = "크루 이름으로 조회",
            description = "크루 이름으로 크루 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 크루")
    })
    ResponseEntity<CrewResponse> findCrewByName(
            @Parameter(description = "크루 이름", example = "홍길동") String name);
}
