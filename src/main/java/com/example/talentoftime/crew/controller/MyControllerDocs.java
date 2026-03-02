package com.example.talentoftime.crew.controller;

import com.example.talentoftime.auth.domain.LoginUser;
import com.example.talentoftime.count.dto.MyCountResponse;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.OnboardingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "마이페이지", description = "로그인한 크루 본인 정보 조회 API")
public interface MyControllerDocs {

    @Operation(
            summary = "온보딩",
            description = "회원가입 이후 닉네임과 크루 유형을 등록합니다.\n\n"
                    + "닉네임 조건: 한글 2~5자, 공백 불가"
    )
    @PostMapping("/onboarding")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "온보딩 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 닉네임 또는 이미 온보딩 완료",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "C005",
                                            summary = "C005 - 이미 온보딩 완료된 크루",
                                            value = "{\"error\": \"C005\", \"message\": \"이미 온보딩 완료된 크루입니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "C006",
                                            summary = "C006 - 닉네임이 공백",
                                            value = "{\"error\": \"C006\", \"message\": \"닉네임은 공백일 수 없습니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "C007",
                                            summary = "C007 - 닉네임 길이 오류 (2~5자)",
                                            value = "{\"error\": \"C007\", \"message\": \"닉네임은 2~5자여야 합니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "C008",
                                            summary = "C008 - 닉네임에 한글 외 문자 포함",
                                            value = "{\"error\": \"C008\", \"message\": \"닉네임은 한글만 사용 가능합니다.\"}"
                                    )
                            }
                    )
            ),
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
            )
    })
    ResponseEntity<CrewResponse> onboardUser(@AuthenticationPrincipal LoginUser loginUser, @Valid @RequestBody OnboardingRequest request);

    @Operation(
            summary = "내 프로필 조회",
            description = "로그인한 크루의 프로필 정보를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
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
            )
    })
    ResponseEntity<CrewResponse> getProfile(@AuthenticationPrincipal LoginUser loginUser);

    @Operation(
            summary = "내 카운트 조회",
            description = "로그인한 크루의 작업 유형별 수행 횟수를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
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
            )
    })
    ResponseEntity<List<MyCountResponse>> getMyCounts(@AuthenticationPrincipal LoginUser loginUser);
}
