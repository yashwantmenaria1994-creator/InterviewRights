package com.example.interviewrights.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class AdminBootstrap {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {

        if (!userRepository.existsByRole("ROLE_ADMIN")) {

            User admin = new User();
            admin.setEmail("admin@interviewrights.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole("ROLE_ADMIN");
            admin.setFirstName("System");
            admin.setLastName("Admin");

            userRepository.save(admin);
        }
    }
}
