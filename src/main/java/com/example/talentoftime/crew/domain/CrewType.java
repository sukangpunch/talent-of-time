package com.example.talentoftime.crew.domain;

import java.time.LocalTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CrewType {

    PREPARING(LocalTime.of(7, 0), LocalTime.of(15, 0)),
    MORNING(LocalTime.of(7, 30), LocalTime.of(15, 30)),
    MIDDLE(LocalTime.of(9, 0), LocalTime.of(17, 0)),
    AFTERNOON(LocalTime.of(14, 30), LocalTime.of(22, 30)),
    TEST(LocalTime.of(0,0), LocalTime.of(23,59));

    private final LocalTime workStart;
    private final LocalTime workEnd;

    public boolean isOnDutyAt(LocalTime time) {
        return !time.isBefore(workStart) && time.isBefore(workEnd);
    }
}
