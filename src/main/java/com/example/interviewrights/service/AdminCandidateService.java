package com.example.interviewrights.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.request.CandidateDto;
import com.example.interviewrights.request.UserRequest;

public interface AdminCandidateService {
    Page<CandidateDto> getCandidates(int page, int size, String keyword);
    User updateCandidate(UUID id, UserRequest user);
    void deleteCandidate(UUID id);
	User getById(UUID id);
	Page<CandidateDto> getPublicCandidates(int page, int size, String keyword);
}

