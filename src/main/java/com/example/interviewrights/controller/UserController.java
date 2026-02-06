package com.example.interviewrights.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.entity.UserInvite;
import com.example.interviewrights.repository.UserInviteRepository;
import com.example.interviewrights.repository.UserRepository;
import com.example.interviewrights.request.InviteRegisterRequest;
import com.example.interviewrights.request.InviteRequest;
import com.example.interviewrights.service.InviteService;
import com.example.interviewrights.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserInviteRepository inviteRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private InviteService inviteService;
	
	
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile() {
        return ResponseEntity.ok(userService.getLoggedInUser());
    }
    
    @PutMapping("/me")
    public ResponseEntity<User> updateMyProfile(
            @RequestBody User user) {

        return ResponseEntity.ok(userService.updateLoggedInUser(user));
    }
    
    @PostMapping("/invite")
    public ResponseEntity<String> inviteUser(@RequestBody InviteRequest request) {

        String token = UUID.randomUUID().toString();

        UserInvite invite = new UserInvite();
        invite.setEmail(request.getEmail());
        invite.setRole("ROLE_USER");
        invite.setInviteToken(token);
        invite.setExpiryTime(LocalDateTime.now().plusHours(24));
        invite.setUrl("http://localhost:8080/admin/register-invite.html?token=" + token);

        inviteRepo.save(invite);

        String inviteUrl =
            "http://localhost:8080/admin/register-invite.html?token=" + token;

        return ResponseEntity.ok(inviteUrl);
    }
    
    @PostMapping("/invite/register")
    public ResponseEntity<String> registerViaInvite(
            @RequestBody InviteRegisterRequest request) {

        UserInvite invite = inviteRepo.findByInviteToken(request.getToken())
            .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (invite.isUsed()) {
            throw new RuntimeException("Already used");
        }

        User user = new User();
        user.setEmail(invite.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(invite.getRole());
        user.setCandidateStatus("invited");
        user.setActive(true);

        userRepo.save(user);

        invite.setUsed(true);
        inviteRepo.save(invite);

        return ResponseEntity.ok("Registration successful");
    }

    @GetMapping("/invite")
    public ResponseEntity<Page<UserInvite>> getAllInvites(@RequestParam(required = false) String email,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(inviteService.getAllInvites(email, status, page, size));
    }
    
}
