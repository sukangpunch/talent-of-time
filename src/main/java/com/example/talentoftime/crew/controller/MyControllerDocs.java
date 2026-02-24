package com.example.talentoftime.crew.controller;

import com.example.talentoftime.count.dto.MyCountResponse;
import com.example.talentoftime.crew.dto.CrewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "마이페이지", description = "로그인한 크루 본인 정보 조회 API")
public interface MyControllerDocs {

    @Operation(
            summary = "내 프로필 조회",
            description = "로그인한 크루의 프로필 정보를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    ResponseEntity<CrewResponse> getProfile(Long crewId);

    @Operation(
            summary = "내 카운트 조회",
            description = "로그인한 크루의 작업 유형별 수행 횟수를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    ResponseEntity<List<MyCountResponse>> getMyCounts(Long crewId);
}
