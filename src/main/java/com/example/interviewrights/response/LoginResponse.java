package com.example.interviewrights.response;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private Long mobile;
    
    public LoginResponse(String token, String email, String firstName, String lastName, Long mobile){
        this.token = token;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;

    }
}
