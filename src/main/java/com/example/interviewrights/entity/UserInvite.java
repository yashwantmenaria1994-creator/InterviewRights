package com.example.interviewrights.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class UserInvite extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String email;

    private String role = "ROLE_USER"; // ROLE_USER / ROLE_ADMIN

    @Column(nullable = false, unique = true)
    private String inviteToken;

    private String url;
    
    private boolean used = false;

    private LocalDateTime expiryTime;

    private LocalDateTime createdAt = LocalDateTime.now();
}
