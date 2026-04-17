package com.example.interviewrights.serviceImpl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.repository.UserRepository;
import com.example.interviewrights.request.CandidateDto;
import com.example.interviewrights.service.AdminCandidateService;

@Service
public class AdminCandidateServiceImpl implements AdminCandidateService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Page<CandidateDto> getCandidates(int page, int size, String keyword) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentUserEmail = auth.getName();

		User currentUser = userRepository.findByEmail(currentUserEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		Page<User> users = userRepository.findByCreatedFrom(currentUser, PageRequest.of(page, size));

		return users.map(user -> new CandidateDto(user.getId(), user.getFirstName(), user.getEmail(), user.getRole(),
				user.isActive()));
	}

	@Override
	public User updateCandidate(UUID id, User updatedUser) {

		User existing = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidate not found"));

		existing.setFirstName(updatedUser.getFirstName());
		existing.setLastName(updatedUser.getLastName());
		existing.setMobile(updatedUser.getMobile());
		existing.setTechnology(updatedUser.getTechnology());
		existing.setExperience(updatedUser.getExperience());

		existing.setCandidateStatus(updatedUser.getCandidateStatus());
		existing.setCountryCode(updatedUser.getCountryCode());
		existing.setDob(updatedUser.getDob());
		existing.setDrivingLicense(updatedUser.getDrivingLicense());
		existing.setGender(updatedUser.getGender());
		existing.setRole(updatedUser.getRole());

		existing.setPostalCode(updatedUser.getPostalCode());
		existing.setPortfolioUrl(updatedUser.getPortfolioUrl());
		existing.setLinkedinUrl(updatedUser.getLinkedinUrl());
		existing.setGithubUrl(updatedUser.getGithubUrl());
		existing.setAddress2(updatedUser.getAddress2());
		existing.setAddress1(updatedUser.getAddress1());

		existing.setWorkAuthorization(updatedUser.getWorkAuthorization());
		existing.setSponsorshipRequired(updatedUser.getSponsorshipRequired());
		existing.setVisaType(updatedUser.getVisaType());
		existing.setVisaExpiry(updatedUser.getVisaExpiry());
		existing.setPassportNumber(updatedUser.getPassportNumber());
		existing.setCitizenshipCountry(updatedUser.getCitizenshipCountry());

		return userRepository.save(existing);
	}

	@Override
	public void deleteCandidate(UUID id) {
		userRepository.deleteById(id);
	}

	@Override
	public User getById(UUID id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidate not found"));
	}

}
