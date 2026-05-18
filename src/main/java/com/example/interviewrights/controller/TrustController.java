package com.example.interviewrights.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.interviewrights.entity.InterviewResultDTO;
import com.example.interviewrights.entity.InterviewSchedule;
import com.example.interviewrights.repository.InterviewRepository;
import com.example.interviewrights.repository.InterviewResultRepo;
import com.example.interviewrights.request.HeartbeatRequest;
import com.example.interviewrights.request.TerminateRequest;
import com.example.interviewrights.request.TrustInitRequest;
import com.example.interviewrights.request.ViolationRequest;
import com.example.interviewrights.serviceImpl.TrustService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trust")
@RequiredArgsConstructor
public class TrustController {

	@Autowired
	private InterviewRepository repo;

	@Autowired
	private TrustService service;

	@Autowired
	private InterviewResultRepo interviewResultRepo;
	
	// 🔹 INIT
	@PostMapping("/init")
	public ResponseEntity<?> init(@RequestBody TrustInitRequest request, HttpServletRequest http) {

		String ip = http.getRemoteAddr();
		return ResponseEntity.ok(service.initSession(request, ip));
	}

	// 🔹 HEARTBEAT
	@PostMapping("/heartbeat")
	public ResponseEntity<?> heartbeat(@RequestBody HeartbeatRequest request) {
		return ResponseEntity.ok(service.heartbeat(request));
	}

	// 🔹 COMPLETE
	@PostMapping("/complete/{interviewId}")
	public ResponseEntity<?> complete(@PathVariable Long interviewId) {
		return ResponseEntity.ok(service.completeSession(interviewId));
	}

	@PostMapping("/terminate")
	public ResponseEntity<?> terminate(@RequestBody TerminateRequest req) {

		InterviewSchedule interview = repo.findById(req.getInterviewId()).orElseThrow();

		interview.setStatus("TERMINATED");
		repo.save(interview);

		return ResponseEntity.ok("terminated");
	}

	@PostMapping("/flag")
	@ResponseBody
	public ResponseEntity<?> flag(@RequestBody ViolationRequest req) {

		try {

			service.flagViolation(req.getInterviewId(), req.getReason());

			return ResponseEntity.ok("Violation Logged");

		} catch (Exception e) {

			return ResponseEntity.status(500).body(e.getMessage());

		}

	}

	@PostMapping("/finish")
	public ResponseEntity<?> finish(@RequestBody InterviewResultDTO dto) {

		interviewResultRepo.save(dto);

		return ResponseEntity.ok().build();

	}

	@GetMapping("/results")
	public List<InterviewResultDTO> getAllResults() {
	    return interviewResultRepo.findAllByOrderByIdDesc();
	}

}