package com.example.interviewrights.request;

import lombok.Data;

@Data
public class InviteRequest {
	private String email;
	private String role; // ROLE_USER / ROLE_ADMIN

	
}
