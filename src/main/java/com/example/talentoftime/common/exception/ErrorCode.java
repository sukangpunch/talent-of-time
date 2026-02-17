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
    SCHEDULE_SWAP_SAME_TASK("S005", "동일한 작업 유형은 교환할 수 없습니다.");

    private final String code;
    private final String message;
}
