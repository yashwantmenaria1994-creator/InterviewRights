package com.example.interviewrights.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.interviewrights.entity.InterviewSchedule;
import com.example.interviewrights.repository.InterviewRepository;
import com.example.interviewrights.request.InterviewRequest;
import com.example.interviewrights.service.EmailService;
import com.example.interviewrights.service.InterviewService;

@Service
public class InterviewServiceImpl implements InterviewService {

	@Autowired
	private InterviewRepository repo;

	@Autowired
	private EmailService emailService;

	public void scheduleInterview(InterviewRequest req) {

		String token = UUID.randomUUID().toString();

		InterviewSchedule interview = new InterviewSchedule();

		interview.setCandidateEmail(req.getEmail());
		interview.setInterviewDate(LocalDate.parse(req.getDate()));
		interview.setInterviewTime(LocalTime.parse(req.getTime()));
		interview.setInterviewLink(req.getLink());
		// interview.setExpiryMinutes(req.getExpiry());
		interview.setStatus("SCHEDULED");
		interview.setCreatedAt(LocalDateTime.now());
		interview.setToken(token);

		// expiry = interview time + minutes
		LocalDateTime expiry = LocalDateTime.of(interview.getInterviewDate(), interview.getInterviewTime())
				.plusMinutes(req.getExpiry());

		interview.setExpiryTime(expiry);
		interview.setStatus("SCHEDULED");
		interview.setCreatedAt(LocalDateTime.now());

		repo.save(interview);
		// 📩 Send Email
	    String secureLink = "http://localhost:8080/interview.html?token=" + token;
		//emailService.sendInterviewMail(req);
	    emailService.sendInterviewMail(req.getEmail(), secureLink);

	}

	@Override
	public ResponseEntity<?> validateInterview(String token) {
		Optional<InterviewSchedule> optional = repo.findByToken(token);

		if (optional.isEmpty()) {
			return ResponseEntity.badRequest().body("Invalid token");
		}

		InterviewSchedule interview = optional.get();

		// expiry check
		if (LocalDateTime.now().isAfter(interview.getExpiryTime())) {

			interview.setStatus("EXPIRED");
			repo.save(interview);

			return ResponseEntity.badRequest().body("Link expired");
		}

		Map<String, Object> res = new HashMap<>();
		res.put("email", interview.getCandidateEmail());
		res.put("link", interview.getInterviewLink());

		return ResponseEntity.ok(res);
	}

	public ResponseEntity<?> complete(String token) {

		Optional<InterviewSchedule> optional = repo.findByToken(token);

		if (optional.isEmpty()) {
			return ResponseEntity.badRequest().body("Invalid token");
		}

		InterviewSchedule interview = optional.get();

		interview.setStatus("COMPLETED");
		repo.save(interview);

		return ResponseEntity.ok("Interview Completed");
	}
}
