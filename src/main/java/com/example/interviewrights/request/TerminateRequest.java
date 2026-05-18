package com.example.interviewrights.request;

import lombok.Data;

@Data
public class TerminateRequest {

    private Long interviewId;

    private String reason; 
    // EXCESSIVE_TAB_SWITCH
    // MANUAL_TERMINATION
    // CHEATING_DETECTED
}