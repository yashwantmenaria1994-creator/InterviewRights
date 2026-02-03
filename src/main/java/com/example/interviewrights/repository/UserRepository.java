package com.example.interviewrights.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.interviewrights.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndIsActiveTrueAndIsDeletedFalse(String email);

	Optional<User> findByResetToken(String token);

	Page<User> findByRole(String role, Pageable pageable);

	Page<User> findByRoleAndFirstNameContainingIgnoreCase(String role, String firstName, Pageable pageable);

	Page<User> findByRoleAndTechnologyContainingIgnoreCase(String role, String technology, Pageable pageable);

	boolean existsByRole(String string);
}