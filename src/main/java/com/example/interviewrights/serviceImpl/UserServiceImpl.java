package com.example.interviewrights.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.repository.UserRepository;
import com.example.interviewrights.request.UserRequest;
import com.example.interviewrights.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private S3Service  s3Service;
	
	@Override
	public User getLoggedInUser() {
		// 1️⃣ SecurityContext se authentication lo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName(); // JWT me subject (email)

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
	}

	 @Override
	    public User updateLoggedInUser(UserRequest updatedUser) {

	        User existing = getLoggedInUser();

	        // ❌ email, role, password update allow nahi
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
	        
	        if (updatedUser.getProfilePic() != null && !updatedUser.getProfilePic().isEmpty()) {
	            String profilePicUrl = s3Service.uploadFile(updatedUser.getProfilePic());
	            existing.setProfilePic(profilePicUrl);   // S3 URL or object key
	        }
	        return userRepository.save(existing);
	    }
	
}
