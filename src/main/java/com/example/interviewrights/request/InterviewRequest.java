package com.example.interviewrights.request;

import lombok.Data;

@Data
public class InterviewRequest {
    private String email;
    private String date;
    private String time;
    private String link;
    private Integer expiry;
}