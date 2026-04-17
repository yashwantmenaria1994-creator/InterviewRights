package com.example.interviewrights.controller;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.interviewrights.entity.InterviewSchedule;
import com.example.interviewrights.repository.InterviewRepository;
import com.example.interviewrights.request.InterviewRequest;
import com.example.interviewrights.request.VerifyRequest;
import com.example.interviewrights.service.InterviewService;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    @Autowired
    private InterviewService service;
    
    @Autowired
    private InterviewRepository repo;

    @PostMapping("/schedule")
    public ResponseEntity<?> schedule(@RequestBody InterviewRequest req) {
        service.scheduleInterview(req);
        return ResponseEntity.ok("Interview Scheduled");
    }
    
    @GetMapping("/count")
    public Map<String, Long> getCounts() {
        Map<String, Long> map = new HashMap<>();

        map.put("scheduled", repo.countByStatus("SCHEDULED"));
        map.put("completed", repo.countByStatus("COMPLETED"));

        return map;
    }
    
    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestParam String token) {
    	 return service.validateInterview(token);

    }
    
    @PostMapping("/complete")
    public ResponseEntity<?> complete(@RequestParam String token) {
    	 return service.validateInterview(token);

    }
    
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyRequest req) {

        Optional<InterviewSchedule> optional = repo.findByToken(req.getToken());

        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false));
        }

        InterviewSchedule interview = optional.get();

        // expiry check
        if (LocalDateTime.now().isAfter(interview.getExpiryTime())) {
            interview.setStatus("EXPIRED");
            repo.save(interview);

            return ResponseEntity.badRequest().body(Map.of("success", false));
        }

        // ✅ save image
        String filePath = saveImage(req.getImage(), interview.getToken());

        // ✅ store path in DB
        interview.setImagePath(filePath);

        repo.save(interview);

        return ResponseEntity.ok(Map.of("success", true));
    }
    
    private String saveImage(String base64Image, String token) {
        try {

            // remove prefix
            String base64 = base64Image.split(",")[1];

            byte[] imageBytes = Base64.getDecoder().decode(base64);

            String filePath = "uploads/" + token + ".png";

            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(imageBytes);
            fos.close();

            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}