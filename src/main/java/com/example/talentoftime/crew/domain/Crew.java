package com.example.talentoftime.crew.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "crew")
public class Crew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "crew_type", nullable = false)
    private CrewType crewType;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private CrewRole role;

    private Crew(String name, CrewType crewType) {
        this.name = name;
        this.crewType = crewType;
        this.role = CrewRole.USER;
    }

    private Crew(
            String name,
            CrewType crewType,
            String username,
            String password,
            String email,
            CrewRole role) {
        this.name = name;
        this.crewType = crewType;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public static Crew create(String name, CrewType crewType) {
        return new Crew(name, crewType);
    }

    public static Crew createWithAuth(
            String name,
            CrewType crewType,
            String username,
            String password,
            String email,
            CrewRole role) {
        return new Crew(name, crewType, username, password, email, role);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateCrewType(CrewType crewType) {
        this.crewType = crewType;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
