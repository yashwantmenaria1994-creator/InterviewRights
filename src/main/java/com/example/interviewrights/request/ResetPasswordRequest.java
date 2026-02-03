package com.example.interviewrights.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String token;
    private String newPassword;

 }
