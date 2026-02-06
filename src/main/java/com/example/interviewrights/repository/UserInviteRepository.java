package com.example.interviewrights.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.interviewrights.entity.UserInvite;

public interface UserInviteRepository extends JpaRepository<UserInvite, UUID>, JpaSpecificationExecutor <UserInvite> {

    Optional<UserInvite> findByInviteToken(String inviteToken);

    Optional<UserInvite> findByEmail(String email);
    
    List<UserInvite> findAllByOrderByCreatedAtDesc();

    Page<UserInvite> findAll(
    	    Specification<UserInvite> filter,
    	    Pageable pageable
    	);
}