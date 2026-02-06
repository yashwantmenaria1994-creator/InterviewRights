package com.example.interviewrights.service;

import org.springframework.data.domain.Page;

import com.example.interviewrights.entity.UserInvite;

public interface InviteService {

	public Page<UserInvite> getAllInvites(String email, String status, int page, int size);

}
