package com.example.interviewrights.request;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.example.interviewrights.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class UserRequest {
	private String email;
	private String password;
	private String role;
	private String firstName;
	private String lastName;
	private MultipartFile  profilePic;
	private MultipartFile uploadResume;
	private String drivingLicense;
	private String countryCode;
	private String gender;
	private String dob;
	private String address1;
	private String address2;
	private String postalCode;
	private String ipAddress;
	private Long mobile;
	private String resetToken;
	private LocalDateTime tokenExpiry;

	private String technology;
	private String experience;
	private String candidateStatus;
	private String linkedinUrl;
	private String githubUrl;
	private String portfolioUrl;

	private LocalDateTime lastLoginAt;
	private Integer loginAttempts;
	private Boolean accountLocked;
	
	private String workAuthorization;
	private String sponsorshipRequired;
	private String visaType;
	private LocalDateTime visaExpiry;
	private String passportNumber;
	private String citizenshipCountry;


}
