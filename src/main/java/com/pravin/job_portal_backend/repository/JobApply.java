package com.pravin.job_portal_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.ApplyJob;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.entity.Job;

public interface JobApply extends JpaRepository<ApplyJob, Long> {
  boolean existsByUserAndJob(User user, Job job);

  List<ApplyJob> findByUser(User user);

  List<ApplyJob> findByJob_PostedBy(User postedBy);

  List<ApplyJob> findByJob(Job job);
}
