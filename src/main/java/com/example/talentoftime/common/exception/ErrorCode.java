package com.example.talentoftime.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    CREW_NOT_FOUND("C001", "존재하지 않는 크루입니다."),
    CREW_NAME_DUPLICATED("C002", "이미 사용 중인 크루 이름입니다."),
    CLASSROOM_NOT_FOUND("R001", "존재하지 않는 강의실입니다."),
    CLASSROOM_ROOM_NUMBER_DUPLICATED("R002", "이미 등록된 호수입니다."),
    PERIOD_NOT_FOUND("P001", "존재하지 않는 교시입니다."),
    PERIOD_NUMBER_DUPLICATED("P002", "이미 등록된 교시 번호입니다."),
    COUNT_NOT_FOUND("CT001", "count 정보를 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND("S001", "존재하지 않는 스케줄입니다."),
    SCHEDULE_ALREADY_ASSIGNED("S002", "이미 배정된 스케줄입니다."),
    SCHEDULE_INVALID_CREW_TYPE("S003", "해당 교시에 배정할 수 없는 크루 유형입니다."),
    SCHEDULE_NO_ELIGIBLE_CREW("S004", "배정 가능한 크루가 없습니다."),
    SCHEDULE_SWAP_SAME_TASK("S005", "동일한 작업 유형은 교환할 수 없습니다."),
    CLASS_SESSION_NOT_FOUND("CS001", "존재하지 않는 수업 일정입니다."),
    CLASS_SESSION_DUPLICATED("CS002", "이미 등록된 수업 일정입니다."),
    CLASS_SESSION_EMPTY_FOR_DATE("CS003", "해당 날짜에 등록된 수업 일정이 없습니다."),
    TEACHER_NOT_FOUND("T001", "존재하지 않는 강사입니다."),
    CREW_USERNAME_DUPLICATED("C003", "이미 사용 중인 아이디입니다."),
    CREW_EMAIL_DUPLICATED("C004", "이미 사용 중인 이메일입니다."),
    AUTH_INVALID_CREDENTIALS("A001", "아이디 또는 비밀번호가 올바르지 않습니다."),
    AUTH_UNAUTHORIZED("A002", "인증이 필요합니다."),
    AUTH_FORBIDDEN("A003", "접근 권한이 없습니다."),
    SCHEDULE_NOT_OWNER("S006", "본인의 스케줄만 삭제할 수 있습니다."),
    SCHEDULE_DUPLICATE_TASK("S007", "해당 슬롯에 동일한 작업이 이미 등록되어 있습니다.");

    private final String code;
    private final String message;
}
