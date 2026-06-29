package com.example.interviewrights.service;

import com.example.interviewrights.request.InterviewRequest;

public interface EmailService {
    void sendResetPasswordEmail(String toEmail, String resetLink);

	void sendInterviewMail(InterviewRequest req);

	void sendInterviewMail(String email, String secureLink);

	void sendInterviewMailForInterviewer(String interviewerEmail, String interviewerLink);
}
