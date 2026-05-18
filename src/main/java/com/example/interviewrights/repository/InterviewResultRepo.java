package com.example.interviewrights.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.interviewrights.entity.InterviewResultDTO;

@Repository
public interface InterviewResultRepo extends JpaRepository<InterviewResultDTO, Long> {

	List<InterviewResultDTO> findAllByOrderByIdDesc();

}
