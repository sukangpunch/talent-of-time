package com.example.talentoftime.crew.dto;

import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.crew.domain.Role;

public record CrewResponse(
        Long id,
        String name,
        CrewType crewType,
        String email,
        Role role,
        boolean isOnboarded
) {
    public static CrewResponse from(Crew crew) {
        return new CrewResponse(
                crew.getId(),
                crew.getName(),
                crew.getCrewType(),
                crew.getEmail(),
                crew.getRole(),
                crew.isOnboarded()
        );
    }
}
