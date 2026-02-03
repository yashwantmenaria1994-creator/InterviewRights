package com.example.interviewrights.service;
public interface EmailService {
    void sendResetPasswordEmail(String toEmail, String resetLink);
}
