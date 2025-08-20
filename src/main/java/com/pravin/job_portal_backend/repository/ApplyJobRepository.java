package com.pravin.job_portal_backend.repository;

import com.pravin.job_portal_backend.entity.ApplyJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyJobRepository extends JpaRepository<ApplyJob, Long> {

    // Find all jobs applied by a specific user
    List<ApplyJob> findByUserId(Long userId);

    // Find all applications for a specific job
    List<ApplyJob> findByJobId(Long jobId);

    // Check if a user already applied for a job
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
