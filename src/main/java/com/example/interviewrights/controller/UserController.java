package com.example.interviewrights.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.entity.UserInvite;
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
    public String inviteUser(@RequestBody InviteRequest request) {
    	return inviteService.inviteUser(request);
    }
    
    @PostMapping("/invite/register")
    public ResponseEntity<String> registerViaInvite(
            @RequestBody InviteRegisterRequest request) {
       return inviteService.registerViaInvite(request);
    	
    }

    @GetMapping("/invite")
    public ResponseEntity<Page<UserInvite>> getAllInvites(@RequestParam(required = false) String email,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(inviteService.getAllInvites(email, status, page, size));
    }
    
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String token) {
        return inviteService.validate(token);
    }
    
    @GetMapping("/{token}")
    public ResponseEntity<User> getByToken(@PathVariable String token) {
        return ResponseEntity.ok(inviteService.getByToken(token));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvite(@PathVariable UUID id) {
        inviteService.deleteInvite(id);
        return ResponseEntity.ok("Invite deleted successfully");
    }
    
    @PostMapping("/regenerate")
    public ResponseEntity<String> regenerate(@RequestParam String email) {
        return ResponseEntity.ok(inviteService.regenerateInvite(email));
    }
}
