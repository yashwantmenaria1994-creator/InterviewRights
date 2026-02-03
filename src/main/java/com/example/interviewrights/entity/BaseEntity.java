package com.example.interviewrights.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {

	@Id
	@GeneratedValue
	private UUID id;
	private boolean isActive = true;
	private boolean isDeleted = false;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;

}
