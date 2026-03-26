package com.example.talentoftime.crew.dto;

import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.crew.domain.Role;

public record CrewSummaryResponse(
        Long id,
        String name,
        CrewType crewType,
        Role role
) {
    public static CrewSummaryResponse from(Crew crew) {
        return new CrewSummaryResponse(
                crew.getId(),
                crew.getName(),
                crew.getCrewType(),
                crew.getRole()
        );
    }
}
