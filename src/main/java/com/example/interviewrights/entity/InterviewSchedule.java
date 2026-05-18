package com.example.interviewrights.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "interview_schedule")
@Data
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String candidateEmail;

    private LocalDate interviewDate;
    private LocalTime interviewTime;

    private String interviewLink;
    private Integer expiryMinutes;

    private String status; // SCHEDULED, COMPLETED, EXPIRED
    private String token;
    private LocalDateTime expiryTime;
    private String imagePath; // 👈 ADD THIS

    private LocalDateTime createdAt;
    private String interviewerEmail;

}