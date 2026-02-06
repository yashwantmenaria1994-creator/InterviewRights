package com.example.interviewrights.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.interviewrights.Utils.UserInviteSpecification;
import com.example.interviewrights.entity.UserInvite;
import com.example.interviewrights.repository.UserInviteRepository;
import com.example.interviewrights.service.InviteService;

@Service
public class InviteServiceImpl implements InviteService {

	@Autowired
	private UserInviteRepository repo;

	@Override
	public Page<UserInvite> getAllInvites(
	        String email,
	        String status,
	        int page,
	        int size
	) {
	    Pageable pageable = PageRequest.of(page, size);

	    return repo.findAll(
	            UserInviteSpecification.filter(email, status),
	            pageable
	    );
	}

}
