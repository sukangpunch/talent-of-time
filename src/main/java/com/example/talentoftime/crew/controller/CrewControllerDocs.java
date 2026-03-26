package com.example.talentoftime.crew.controller;

import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.CrewSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "크루", description = "크루(알바생) 조회 API")
public interface CrewControllerDocs {

    @Operation(
            summary = "크루 전체 조회",
            description = "등록된 모든 크루의 목록을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    ResponseEntity<List<CrewSummaryResponse>> findAllCrews();

    @Operation(
            summary = "크루 이름으로 조회",
            description = "크루 이름으로 크루 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 크루",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "C001",
                                    summary = "C001 - 존재하지 않는 크루",
                                    value = "{\"error\": \"C001\", \"message\": \"존재하지 않는 크루입니다.\"}"
                            )
                    )
            )
    })
    @GetMapping("/name")
    ResponseEntity<CrewResponse> findCrewByName(
            @Parameter(description = "크루 이름", example = "홍길동") String name);
}
