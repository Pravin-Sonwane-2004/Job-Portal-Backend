package com.pravin.job_portal_backend.repository;

import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.JobApplication;
import com.pravin.job_portal_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByUserAndJob(User user, Job job);

    List<JobApplication> findByUserId(Long userId);

    List<JobApplication> findByJobId(Long jobId);

    Optional<JobApplication> findByUserIdAndJobId(Long userId, Long jobId);
}
