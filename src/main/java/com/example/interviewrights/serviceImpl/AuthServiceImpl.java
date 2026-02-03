package com.example.interviewrights.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.repository.UserRepository;
import com.example.interviewrights.request.LoginRequest;
import com.example.interviewrights.request.RegisterRequest;
import com.example.interviewrights.request.ResetPasswordRequest;
import com.example.interviewrights.response.LoginResponse;
import com.example.interviewrights.security.JwtUtil;
import com.example.interviewrights.service.AuthService;
import com.example.interviewrights.service.EmailService;

import jakarta.validation.Valid;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

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
		user.setRole("ROLE_USER");
		userRepository.save(user);
	}

	public LoginResponse login(LoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid email or password");
		}

		String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

		return new LoginResponse(token, user.getEmail(), user.getFirstName(), user.getLastName(), user.getMobile(),user.getRole());
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

	@Override
	public void sendResetLink(String email) {

		Optional<User> userOpt = userRepository.findByEmail(email);

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			String token = UUID.randomUUID().toString();

			user.setResetToken(token);
			user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));

			userRepository.save(user);

			String resetLink = "http://localhost:8080/reset-password.html?token=" + token;

			emailService.sendResetPasswordEmail(user.getEmail(), resetLink);

		}
	}

	@Override
	public void resetPassword(ResetPasswordRequest request) {

		User user = userRepository.findByResetToken(request.getToken())
				.orElseThrow(() -> new RuntimeException("Invalid token"));

		if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Token expired");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		user.setResetToken(null);
		user.setTokenExpiry(null);

		userRepository.save(user);
	}

	public class InvalidCredentialsException extends RuntimeException {
		public InvalidCredentialsException(String message) {
			super(message);
		}
	}

}
