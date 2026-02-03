package com.example.interviewrights.response;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private Long mobile;
    private String role;

    
    public LoginResponse(String token, String email, String firstName, String lastName, Long mobile, String role){
        this.token = token;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.role = role;

    }
}
