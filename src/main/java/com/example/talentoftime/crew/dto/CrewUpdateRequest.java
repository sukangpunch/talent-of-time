package com.example.talentoftime.crew.dto;

import com.example.talentoftime.crew.domain.CrewType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CrewUpdateRequest {

    private String name;

    private CrewType crewType;
}
