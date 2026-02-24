package com.example.talentoftime.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Crew
    CREW_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "존재하지 않는 크루입니다."),
    CREW_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "C002", "이미 사용 중인 크루 이름입니다."),
    CREW_USERNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "C003", "이미 사용 중인 아이디입니다."),
    CREW_EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "C004", "이미 사용 중인 이메일입니다."),
    CREW_ALREADY_ONBOARDED(HttpStatus.BAD_REQUEST, "C005", "이미 온보딩 완료된 크루입니다."),
    CREW_NICKNAME_BLANK(HttpStatus.BAD_REQUEST, "C006", "닉네임은 공백일 수 없습니다."),
    CREW_NICKNAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "C007", "닉네임은 2~5자여야 합니다."),
    CREW_NICKNAME_PATTERN_INVALID(HttpStatus.BAD_REQUEST, "C008", "닉네임은 한글만 사용 가능합니다."),

    // Classroom
    CLASSROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "존재하지 않는 강의실입니다."),
    CLASSROOM_ROOM_NUMBER_DUPLICATED(HttpStatus.BAD_REQUEST, "R002", "이미 등록된 호수입니다."),

    // Period
    PERIOD_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "존재하지 않는 교시입니다."),
    PERIOD_NUMBER_DUPLICATED(HttpStatus.BAD_REQUEST, "P002", "이미 등록된 교시 번호입니다."),

    // Count
    COUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "CT001", "count 정보를 찾을 수 없습니다."),

    // Schedule
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "존재하지 않는 스케줄입니다."),
    SCHEDULE_ALREADY_ASSIGNED(HttpStatus.CONFLICT, "S002", "이미 배정된 스케줄입니다."),
    SCHEDULE_INVALID_CREW_TYPE(HttpStatus.BAD_REQUEST, "S003", "해당 교시에 배정할 수 없는 크루 유형입니다."),
    SCHEDULE_NO_ELIGIBLE_CREW(HttpStatus.BAD_REQUEST, "S004", "배정 가능한 크루가 없습니다."),
    SCHEDULE_NOT_OWNER(HttpStatus.FORBIDDEN, "S006", "본인의 스케줄만 삭제할 수 있습니다."),

    // ClassSession
    CLASS_SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "CS001", "존재하지 않는 수업 일정입니다."),
    CLASS_SESSION_DUPLICATED(HttpStatus.CONFLICT, "CS002", "이미 등록된 수업 일정입니다."),
    CLASS_SESSION_EMPTY_FOR_DATE(HttpStatus.NOT_FOUND, "CS003", "해당 날짜에 등록된 수업 일정이 없습니다."),
    CLASS_SESSION_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "CS004", "이미 휴강 처리된 수업 일정입니다."),

    // Teacher
    TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "존재하지 않는 강사입니다."),

    // Auth
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A001", "아이디 또는 비밀번호가 올바르지 않습니다."),
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A002", "인증이 필요합니다."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "A003", "접근 권한이 없습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A004", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A005", "만료된 토큰입니다."),
    TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "A006", "토큰이 비어있습니다."),
    TOKEN_NOT_SIGNED(HttpStatus.UNAUTHORIZED, "A007", "서명되지 않은 토큰입니다."),

    // OAuth
    DEST_NOT_VALID(HttpStatus.BAD_REQUEST, "O001", "올바르지 않은 DEST 형식입니다."),
    OAUTH_SERVER_ERROR(HttpStatus.BAD_GATEWAY, "O002", "OAuth 서버 오류가 발생했습니다."),
    AUTH_CODE_INVALID(HttpStatus.BAD_REQUEST, "O003", "유효하지 않은 인증 코드입니다."),
    OAUTH_ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "O004", "유효하지 않은 OAuth 액세스 토큰입니다."),
    PROVIDER_INVALID(HttpStatus.BAD_REQUEST, "O005", "지원하지 않는 OAuth 제공자입니다."),

    // Global
    INVALID_PARAMS(HttpStatus.BAD_REQUEST, "G001", "유효하지 않은 요청 파라미터입니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G002", "데이터베이스 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G003", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
