package com.example.talentoftime.crew.domain;

import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
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
    @Column(name = "crew_type")
    private CrewType crewType;

    @Column(name = "email")
    private String email;

    @Column(name = "provider_id", unique = true)
    private String providerId;

    @Column(name = "is_onboarded", nullable = false)
    private boolean isOnboarded;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    private Crew(String name, CrewType crewType) {
        this.name = name;
        this.crewType = crewType;
        this.role = Role.USER;
    }

    public Crew(
            String name,
            String email,
            String providerId,
            Role role
    ) {
        this.name = name;
        this.email = email;
        this.providerId = providerId;
        this.role = role;
    }

    public void onboard(
            String name,
            CrewType crewType
    ){
        validateOnboard(name);
        this.name = name;
        this.crewType = crewType;
        this.isOnboarded = true;
    }

    private void validateOnboard(
            String nickname
    ) {
        validateIsOnboarded();
        validateNickname(nickname);
    }

    private void validateIsOnboarded() {
        if (this.isOnboarded()) {
            throw new BusinessException(ErrorCode.CREW_ALREADY_ONBOARDED);
        }
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new BusinessException(ErrorCode.CREW_NICKNAME_BLANK);
        }

        if (nickname.length() < 2 || nickname.length() > 5) {
            throw new BusinessException(ErrorCode.CREW_NICKNAME_LENGTH_INVALID);
        }

        if (!nickname.matches("^[가-힣]+$")) {
            throw new BusinessException(ErrorCode.CREW_NICKNAME_PATTERN_INVALID);
        }
    }
}
