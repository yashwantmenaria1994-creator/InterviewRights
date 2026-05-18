package com.example.interviewrights.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class InterviewResultDTO extends BaseEntity {

 private String interviewId;
	
 private String candidateEmail;

 private String room;

 private int riskScore;

 private int tabViolations;

 private int faceViolations;

 private int multipleFaceViolations;

 private int interviewDuration;

 private boolean passed;
 
 private Double faceSimilarity;
}