package com.example.interviewrights.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	boolean existsByEmail(String email);

	Page<User> findByCreatedBy(User currentUser, PageRequest of);

	Page<User> findByCreatedFrom(User currentUser, PageRequest of);

	@Query("""
			    SELECT DISTINCT u
			    FROM User u
			    LEFT JOIN u.skills s
			    WHERE u.createdFrom IS NULL
			    AND (
			        LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			        OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			        OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
			        OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    )
			""")
	Page<User> findPublicCandidatesByKeyword(@Param("keyword") String keyword, Pageable pageable);

	Page<User> findByCreatedFromIsNull(PageRequest of);
}