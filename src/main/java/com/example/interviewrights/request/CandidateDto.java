package com.example.interviewrights.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDto {
    private UUID id;
    private String name;
    private String email;
    private String role;
    private boolean active;
}