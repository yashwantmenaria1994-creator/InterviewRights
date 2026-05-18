package com.example.interviewrights.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.interviewrights.entity.TrustSession;

@Repository
public interface TrustSessionRepository extends JpaRepository<TrustSession, Long> {

    Optional<TrustSession> findByInterviewId(Long interviewId);
}