package com.example.interviewrights.request;

import lombok.Data;

@Data
public class VerifyRequest {
    private String token;
    private String image; // base64 image
}