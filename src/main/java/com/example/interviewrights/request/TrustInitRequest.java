package com.example.interviewrights.request;

import lombok.Data;

@Data
public class TrustInitRequest {
    private Long interviewId;
    private String candidateEmail;
    private String deviceFingerprint;
}