package com.example.interviewrights.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String role;
    private boolean isActive = true;
    private boolean isDeleted = false;
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
  
}
