package com.example.interviewrights.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.entity.UserInvite;
import com.example.interviewrights.request.InviteRegisterRequest;
import com.example.interviewrights.request.InviteRequest;

public interface InviteService {

	public Page<UserInvite> getAllInvites(String email, String status, int page, int size);
	
	public String inviteUser(InviteRequest request);
	
	public ResponseEntity<String> registerViaInvite(InviteRegisterRequest request);
	
	public ResponseEntity<String> validate(String token);

	public User getByToken(String token);

	void deleteInvite(UUID id);

	public String regenerateInvite(String email);	

}
