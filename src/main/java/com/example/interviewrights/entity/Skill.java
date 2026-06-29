package com.example.interviewrights.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Skill extends BaseEntity {

    private String name;
}