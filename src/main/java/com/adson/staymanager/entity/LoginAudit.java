package com.adson.staymanager.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LoginAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String email;
    private String ipAddress;
    private String userAgent;
    @Enumerated(EnumType.STRING)
    private LoginOutcome outcome; // "SUCCESS" ou "FAILURE"
    private LocalDateTime timestamp;
    private String failureReason;

    public LoginAudit(Long userId, String email, String ipAddress, String userAgent, LoginOutcome outcome) {
        this.userId = userId;
        this.email = email;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.outcome = outcome;
        this.timestamp = LocalDateTime.now();
    }

    public enum LoginOutcome {
        SUCCESS,
        FAILURE
    }
}