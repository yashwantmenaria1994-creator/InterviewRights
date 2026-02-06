package com.example.interviewrights.service;

import com.example.interviewrights.entity.User;

public interface UserService {

	User getLoggedInUser();

	User updateLoggedInUser(User user);

}
