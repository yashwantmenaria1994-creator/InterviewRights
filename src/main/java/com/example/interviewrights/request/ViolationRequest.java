package com.example.interviewrights.request;

import lombok.Data;

@Data
public class ViolationRequest {
   private Long interviewId;
   private String reason;
}