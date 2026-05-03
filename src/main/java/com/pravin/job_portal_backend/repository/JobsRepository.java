package com.pravin.job_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.User;

import java.util.List;

public interface JobsRepository extends JpaRepository<Job, Long> {
  List<Job> findByPostedBy(User postedBy);
  long countByPostedBy(User postedBy);
  List<Job> findByCompanyIgnoreCase(String company);
  long countByCompanyIgnoreCase(String company);
}
