package com.example.talentoftime.crew.dto;

import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewRole;
import com.example.talentoftime.crew.domain.CrewType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewResponse {

    private Long id;
    private String name;
    private CrewType crewType;
    private String username;
    private String email;
    private CrewRole role;

    public static CrewResponse from(Crew crew) {
        CrewResponse response = new CrewResponse();
        response.id = crew.getId();
        response.name = crew.getName();
        response.crewType = crew.getCrewType();
        response.username = crew.getUsername();
        response.email = crew.getEmail();
        response.role = crew.getRole();
        return response;
    }
}
