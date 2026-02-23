package com.example.talentoftime.count.dto;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.count.domain.Count;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyCountResponse {

    private TaskType taskType;
    private int count;

    public static MyCountResponse from(Count count) {
        MyCountResponse response = new MyCountResponse();
        response.taskType = count.getTaskType();
        response.count = count.getCount();
        return response;
    }
}
