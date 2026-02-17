package com.example.talentoftime.crew.dto;

import com.example.talentoftime.crew.domain.Crew;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewResponse {

    private Long id;
    private String name;
    private String crewType;

    public static CrewResponse from(Crew crew) {
        CrewResponse response = new CrewResponse();
        response.id = crew.getId();
        response.name = crew.getName();
        response.crewType = crew.getCrewType().name();
        return response;
    }
}
