package com.example.interviewrights.service;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.request.UserRequest;

public interface UserService {

	User getLoggedInUser();

	User updateLoggedInUser(UserRequest user);

}
