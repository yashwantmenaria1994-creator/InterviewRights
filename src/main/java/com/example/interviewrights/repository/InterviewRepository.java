package com.example.interviewrights.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.interviewrights.entity.InterviewSchedule;

public interface InterviewRepository extends JpaRepository<InterviewSchedule, Long> {

    long countByStatus(String status);
    
    Optional<InterviewSchedule> findByToken(String token);

    boolean existsByToken(String token);


}