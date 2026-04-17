package com.example.interviewrights.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class UserSession {

    @Id
    private UUID userId;

    private String token;

    private LocalDateTime loginTime;
}