package com.example.interviewrights.Utils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

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