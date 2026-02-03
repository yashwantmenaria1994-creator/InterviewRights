package com.example.interviewrights.service;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.request.LoginRequest;
import com.example.interviewrights.request.RegisterRequest;
import com.example.interviewrights.request.ResetPasswordRequest;
import com.example.interviewrights.response.LoginResponse;

import jakarta.validation.Valid;

public interface AuthService {

	void register(@Valid RegisterRequest request);

	LoginResponse login(LoginRequest request);

	User findByEmail(String username);
	
	 void sendResetLink(String email);

	 void resetPassword(ResetPasswordRequest request);

}
