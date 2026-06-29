package com.example.interviewrights;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.repository.UserRepository;

@Configuration
public class InsertAdminUser {

	 @Bean
	    CommandLineRunner createSuperAdmin(UserRepository userRepository,
	                                       PasswordEncoder passwordEncoder) {

	        return args -> {

	            if (!userRepository.existsByEmail("superadmin@interviewrights.com")) {

	                User user = new User();
	                user.setEmail("superadmin@interviewrights.com");
	                user.setPassword(passwordEncoder.encode("Admin@123"));
	                user.setRole("ROLE_ADMIN");
	                user.setFirstName("Super");
	                user.setLastName("Admin");
	                user.setActive(true);
	                user.setDeleted(false);

	                userRepository.save(user);

	                System.out.println("✅ Super Admin created.");
	            } else {
	                System.out.println("✅ Super Admin already exists.");
	            }
	        };
	    }
	}