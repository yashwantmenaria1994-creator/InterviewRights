package com.example.interviewrights.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.example.interviewrights.enums.CandidateOwnershipStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
	private String noticePeriod;
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
	
	private String workAuthorization;
	private String sponsorshipRequired;
	private String visaType;
	private LocalDateTime visaExpiry;
	private String passportNumber;
	private String citizenshipCountry;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_From")
	private User createdFrom;
	
	@Enumerated(EnumType.STRING)
	private CandidateOwnershipStatus ownershipStatus;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	        name = "user_skill_mapping",
	        joinColumns = @JoinColumn(name = "user_id"),
	        inverseJoinColumns = @JoinColumn(name = "skill_id")
	)
	private Set<Skill> skills = new HashSet<>();
}
