package com.example.interviewrights.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.interviewrights.entity.UserSession;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, UUID> {

	Optional<UserSession> findByUserId(UUID id);

}
