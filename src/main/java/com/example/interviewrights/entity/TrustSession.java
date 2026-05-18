package com.example.interviewrights.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TrustSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long interviewId;
    private String candidateEmail;

    private String deviceFingerprint;
    private String ipAddress;

    private LocalDateTime startTime;
    private LocalDateTime lastHeartbeatTime;

    private Integer riskScore = 100;
    private Integer flagsCount = 0;

    private String status; // ACTIVE, FLAGGED, COMPLETED
}