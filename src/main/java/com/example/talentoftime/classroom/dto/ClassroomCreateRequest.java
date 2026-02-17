package com.example.talentoftime.classroom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClassroomCreateRequest {

    @NotNull
    private Integer roomNumber;
}
