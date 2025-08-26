package com.pravin.job_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.Recruiter;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    boolean existsByUser_Id(Long userId);
}
