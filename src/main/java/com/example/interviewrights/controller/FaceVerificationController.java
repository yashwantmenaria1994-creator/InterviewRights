package com.example.interviewrights.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.interviewrights.service.FaceVerificationService;

@RestController
@RequestMapping("/api/face")
public class FaceVerificationController {

	@Autowired
    private FaceVerificationService faceVerificationService;

    @PostMapping("/verify")
    public Map<String, Object> verify(
            @RequestParam MultipartFile file,
            @RequestParam String candidateEmail) {

    	 return faceVerificationService.verifyFace(candidateEmail, file);
    }
}