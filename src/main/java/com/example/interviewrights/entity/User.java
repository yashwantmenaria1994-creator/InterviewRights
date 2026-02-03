package com.example.interviewrights.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {

	@Column(unique = true, nullable = false)
	private String email;
	private String password;
	private String role;
	private String firstName;
	private String lastName;
	private String profilePic;
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
	private String uploadResume;
	private String portfolioUrl;

	private LocalDateTime lastLoginAt;
	private Integer loginAttempts;
	private Boolean accountLocked;

}
