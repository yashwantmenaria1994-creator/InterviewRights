package com.example.interviewrights.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.request.CandidateDto;

public interface AdminCandidateService {
    Page<CandidateDto> getCandidates(int page, int size, String keyword);
    User updateCandidate(UUID id, User user);
    void deleteCandidate(UUID id);
	User getById(UUID id);
}

