package com.example.interviewrights.serviceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.interviewrights.entity.TrustSession;
import com.example.interviewrights.repository.TrustSessionRepository;
import com.example.interviewrights.request.HeartbeatRequest;
import com.example.interviewrights.request.TrustInitRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrustService {

	private final TrustSessionRepository repository;

	// 🔹 INIT SESSION
	public TrustSession initSession(TrustInitRequest request, String ip) {

		Optional<TrustSession> existing = repository.findByInterviewId(request.getInterviewId());

		if (existing.isPresent()) {
			return existing.get();
		}

		TrustSession session = new TrustSession();

		session.setInterviewId(request.getInterviewId());
		session.setCandidateEmail(request.getCandidateEmail());
		session.setDeviceFingerprint(request.getDeviceFingerprint());
		session.setIpAddress(ip);

		session.setStartTime(LocalDateTime.now());

		session.setLastHeartbeatTime(LocalDateTime.now());

		session.setFlagsCount(0);
		session.setRiskScore(100);
		session.setStatus("ACTIVE");

		return repository.save(session);

	}

	// 🔹 HEARTBEAT CHECK
	public TrustSession heartbeat(HeartbeatRequest request) {

		if (request.getInterviewId() == null) {
			throw new RuntimeException("Interview Id missing");
		}

		TrustSession session = repository.findByInterviewId(request.getInterviewId())
				.orElseThrow(() -> new RuntimeException("Session not found"));

		LocalDateTime now = LocalDateTime.now();

		/* FIRST HEARTBEAT */
		if (session.getLastHeartbeatTime() == null) {

			session.setLastHeartbeatTime(now);

			return repository.save(session);
		}

		/* normal heartbeat */
		long seconds = Duration.between(session.getLastHeartbeatTime(), now).getSeconds();

		if (seconds > 15) {

			session.setFlagsCount(session.getFlagsCount() + 1);

			session.setRiskScore(session.getRiskScore() - 10);

		}

		session.setLastHeartbeatTime(now);

		if (session.getRiskScore() < 70) {
			session.setStatus("FLAGGED");
		}

		return repository.save(session);
	}

	// 🔹 DEVICE CHECK (OPTIONAL EXTENSION)
	public void validateDevice(Long interviewId, String fingerprint) {

		TrustSession session = repository.findByInterviewId(interviewId)
				.orElseThrow(() -> new RuntimeException("Session not found"));

		if (!session.getDeviceFingerprint().equals(fingerprint)) {
			session.setFlagsCount(session.getFlagsCount() + 1);
			session.setRiskScore(session.getRiskScore() - 30);
			session.setStatus("FLAGGED");
			repository.save(session);
		}
	}

	// 🔹 FINAL SCORE
	public TrustSession completeSession(Long interviewId) {

		TrustSession session = repository.findByInterviewId(interviewId)
				.orElseThrow(() -> new RuntimeException("Session not found"));

		session.setStatus("COMPLETED");
		return repository.save(session);
	}

	public void flagViolation(Long interviewId, String reason) {

		TrustSession session = repository.findByInterviewId(interviewId)
				.orElseThrow(() -> new RuntimeException("Session not found"));

		session.setFlagsCount(session.getFlagsCount() + 1);

		switch (reason) {

		case "TAB_SWITCH":
			session.setRiskScore(session.getRiskScore() - 15);
			break;

		case "EXIT_FULLSCREEN":
			session.setRiskScore(session.getRiskScore() - 20);
			break;

		case "DEVTOOLS_OPEN":
			session.setRiskScore(session.getRiskScore() - 25);
			break;

		}

		if (session.getRiskScore() < 40) {
			session.setStatus("TERMINATED");
		} else if (session.getRiskScore() < 60) {
			session.setStatus("FLAGGED");
		}

		repository.save(session);

	}
}