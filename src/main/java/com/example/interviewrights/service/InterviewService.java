package com.example.interviewrights.service;

import org.springframework.http.ResponseEntity;

import com.example.interviewrights.request.InterviewRequest;

public interface InterviewService {

	void scheduleInterview(InterviewRequest req);

	ResponseEntity<?> validateInterview(String token);

	ResponseEntity<?> complete(String token);

}
