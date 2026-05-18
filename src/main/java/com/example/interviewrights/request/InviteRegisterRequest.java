package com.example.interviewrights.request;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class InviteRegisterRequest {

	private String token;
	private String email;
	private String password;
	private String role;
	private String firstName;
	private String lastName;
	private MultipartFile profilePic;
	private String drivingLicense;
	private String countryCode;
	private String gender;
	private String dob;
	private String address1;
	private String address2;
	private String postalCode;
	private String ipAddress;
	private Long mobile;
	private String technology;
	private String experience;
	private String noticePeriod;
	private String linkedinUrl;
	private String githubUrl;
	private String uploadResume;
	private Boolean accountLocked;

}
