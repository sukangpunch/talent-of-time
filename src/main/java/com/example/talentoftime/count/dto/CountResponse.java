package com.example.talentoftime.count.dto;

import com.example.talentoftime.count.domain.Count;

public record CountResponse(
        Long crewId,
        String crewName,
        String taskType,
        int count

) {
    public static CountResponse from(Count count) {
        return new CountResponse(
                count.getId(),
                count.getCrew().getName(),
                count.getTaskType().name(),
                count.getCount()
        );
    }
}
