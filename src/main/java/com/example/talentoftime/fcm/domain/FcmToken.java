package com.example.talentoftime.fcm.domain;

import com.example.talentoftime.crew.domain.Crew;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "fcm_token")
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fcm_token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    public FcmToken(Crew crew, String token) {
        this.crew = crew;
        this.token = token;
    }

    public void updateCrew(Crew crew) {
        this.crew = crew;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}
