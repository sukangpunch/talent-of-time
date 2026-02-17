package com.example.talentoftime.crew.dto;

import com.example.talentoftime.crew.domain.CrewType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CrewCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private CrewType crewType;
}
