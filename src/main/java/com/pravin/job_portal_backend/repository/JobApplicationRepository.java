package com.pravin.job_portal_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.*;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    // List<JobApplication> findByJobSeeker(User user);

    List<JobApplication> findByJob(Job job);
}
