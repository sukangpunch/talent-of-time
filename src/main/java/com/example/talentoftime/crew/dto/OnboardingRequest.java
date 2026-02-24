package com.example.talentoftime.crew.dto;

import com.example.talentoftime.crew.domain.CrewType;

public record OnboardingRequest(
        String name,
        CrewType crewType
) {

}
