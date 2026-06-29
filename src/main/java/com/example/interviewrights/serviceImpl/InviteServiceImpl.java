package com.example.interviewrights.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.interviewrights.Utils.UserInviteSpecification;
import com.example.interviewrights.entity.User;
import com.example.interviewrights.entity.UserInvite;
import com.example.interviewrights.repository.UserInviteRepository;
import com.example.interviewrights.repository.UserRepository;
import com.example.interviewrights.request.InviteRegisterRequest;
import com.example.interviewrights.request.InviteRequest;
import com.example.interviewrights.service.InviteService;

@Service
public class InviteServiceImpl implements InviteService {

	@Autowired
	private UserInviteRepository repo;

	@Autowired
	private UserInviteRepository inviteRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	S3Service s3Service;

	@Value("${app.base-url}")
	private String baseUrl;

	@Override
	public Page<UserInvite> getAllInvites(String email, String status, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentUserEmail = auth.getName();

		User currentUser = userRepo.findByEmail(currentUserEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return repo.findAll(UserInviteSpecification.filter(email, status, currentUser), pageable);
	}

	@Override
	public String inviteUser(InviteRequest request) {
		String token = UUID.randomUUID().toString();

		String email = request.getEmail();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentUserEmail = auth.getName();
		User currentUser = userRepo.findByEmail(currentUserEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Check user already exists
		if (userRepo.existsByEmail(email)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists");
		}

		// Check invite already sent
		Optional<UserInvite> existingInvite = inviteRepo.findByEmailAndInvitedBy(email, currentUser);

		if (existingInvite.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invite already sent to this email");
		}

		UserInvite invite = new UserInvite();
		invite.setEmail(request.getEmail());
		invite.setRole("ROLE_USER");
		invite.setInviteToken(token);
		invite.setExpiryTime(LocalDateTime.now().plusHours(24));
		invite.setUrl(baseUrl+"/admin/register-invite.html?token=" + token);
		invite.setInvitedBy(currentUser);
		inviteRepo.save(invite);

		User user = new User();
		user.setEmail(invite.getEmail());
		user.setRole("ROLE_USER");
		user.setCreatedFrom(currentUser);
		userRepo.save(user);

		String inviteUrl = baseUrl+"/admin/register-invite.html?token=" + token;

		return inviteUrl;
	}

	@Override
	public ResponseEntity<String> registerViaInvite(InviteRegisterRequest request) {

		UserInvite invite = inviteRepo.findByInviteToken(request.getToken()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired invite token"));

		User user = userRepo.findByEmail(invite.getEmail())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for this invite"));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setMobile(request.getMobile());
		user.setCandidateStatus("INVITED");
		user.setActive(true);
		user.setAddress1(request.getAddress1());
		user.setAddress2(request.getAddress2());
		user.setCountryCode(request.getCountryCode());
		user.setDob(request.getDob());
		user.setDrivingLicense(request.getDrivingLicense());
		user.setExperience(request.getExperience());
		user.setGender(request.getGender());
		user.setGithubUrl(request.getGithubUrl());
		user.setTechnology(request.getTechnology());
		user.setRole("ROLE_USER");
		if (request.getProfilePic() != null && !request.getProfilePic().isEmpty()) {
			String profilePicUrl = s3Service.uploadFile(request.getProfilePic());
			user.setProfilePic(profilePicUrl); // S3 URL or object key
		}
		userRepo.save(user);

		invite.setUsed(true);
		inviteRepo.save(invite);

		return ResponseEntity.ok("Registration successful");
	}

	@Override
	public ResponseEntity<String> validate(String token) {
		UserInvite invite = inviteRepo.findByInviteToken(token).orElseThrow(() -> new RuntimeException("Invalid link"));

		if (!invite.isActive())
			return ResponseEntity.badRequest().body("Link inactive");

		if (invite.getExpiryTime().isBefore(LocalDateTime.now()))
			return ResponseEntity.badRequest().body("Link expired");

		return ResponseEntity.ok("Valid");
	}

	@Override
	public User getByToken(String token) {
		if (token == null || token.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is required");
		}

		UserInvite invite = inviteRepo.findByInviteToken(token).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired invite link"));

		return userRepo.findByEmail(invite.getEmail())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for this invite"));
	}

	@Override
	public void deleteInvite(UUID id) {

		Optional<UserInvite> findById = inviteRepo.findById(id);
		if (findById.isPresent()) {

			Optional<User> findByEmail = userRepo.findByEmail(findById.get().getEmail());
			if (findById.isPresent()) {
				userRepo.deleteById(findById.get().getId());
			}
		}
		inviteRepo.deleteById(id);
	}

	@Override
	public String regenerateInvite(String email) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
			throw new RuntimeException("User is not authenticated");
		}
		String currentUserEmail = auth.getName();
		User currentUser = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		UserInvite invite = inviteRepo.findByEmailAndInvitedBy(email, currentUser)
				.orElseThrow(() -> new RuntimeException("Invite not found"));

		// update token
		invite.setInviteToken(UUID.randomUUID().toString());

		// update expiry time (example: +24 hours)
		invite.setExpiryTime(LocalDateTime.now().plusHours(24));

		inviteRepo.save(invite);

		return baseUrl+"/register.html?token=" + invite.getInviteToken();
	}

}
