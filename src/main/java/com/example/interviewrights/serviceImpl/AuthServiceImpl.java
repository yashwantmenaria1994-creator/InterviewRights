package com.example.interviewrights.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.repository.UserRepository;
import com.example.interviewrights.request.LoginRequest;
import com.example.interviewrights.request.RegisterRequest;
import com.example.interviewrights.response.LoginResponse;
import com.example.interviewrights.security.JwtUtil;
import com.example.interviewrights.service.AuthService;

import jakarta.validation.Valid;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;

	public void register(@Valid RegisterRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("User already exists");
		}
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setMobile(request.getMobile());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		userRepository.save(user);
	}

	public LoginResponse login(LoginRequest request) {

		try {
			User user = userRepository.findByEmail(request.getEmail())
					.orElseThrow(() -> new RuntimeException("Invalid credentials"));

			if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
				throw new RuntimeException("Invalid credentials");
			}
			String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
		    return new LoginResponse(
		            token,
		            user.getEmail(),
		            user.getFirstName(),
		            user.getLastName(),
		            user.getMobile()
		            );
			 
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmailAndIsActiveTrueAndIsDeletedFalse(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
	}
	
	public class ResourceNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 5561938392284745921L;

		public ResourceNotFoundException(String message) {
			super(message);
		}
	}

}
