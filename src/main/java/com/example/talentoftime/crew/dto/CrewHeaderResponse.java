package com.example.talentoftime.crew.dto;

import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;

public record CrewHeaderResponse(
        String name,
        CrewType crewType
) {
    public static CrewHeaderResponse from(Crew crew) {
        return new CrewHeaderResponse(
                crew.getName(),
                crew.getCrewType()
        );
    }
}
