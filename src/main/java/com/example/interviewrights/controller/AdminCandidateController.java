package com.example.interviewrights.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.request.CandidateDto;
import com.example.interviewrights.service.AdminCandidateService;

@RestController
@RequestMapping("/api/admin/candidates")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminCandidateController {

	    @Autowired
	    private AdminCandidateService service;

	    
	    @GetMapping("/{id}")
	    public ResponseEntity<User> getById(@PathVariable UUID id) {
	        return ResponseEntity.ok(service.getById(id));
	    }
	    
	    // ✅ View All with Pagination + Filter
	    @GetMapping
	    public ResponseEntity<Page<CandidateDto>> getCandidates(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestParam(required = false) String keyword) {

	        return ResponseEntity.ok(service.getCandidates(page, size, keyword));
	    }

	    // ✅ Update Candidate
	    @PutMapping("/{id}")
	    public ResponseEntity<User> update(
	            @PathVariable UUID id,
	            @RequestBody User user) {

	        return ResponseEntity.ok(service.updateCandidate(id, user));
	    }

	    // ✅ Delete Candidate
	    @DeleteMapping("/{id}")
	    public ResponseEntity<String> delete(@PathVariable UUID id) {
	        service.deleteCandidate(id);
	        return ResponseEntity.ok("Candidate deleted successfully");
	    }
	}
	