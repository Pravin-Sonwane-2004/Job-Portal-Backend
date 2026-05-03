package com.pravin.job_portal_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.JobAlert;
import com.pravin.job_portal_backend.entity.User;

public interface JobAlertRepository extends JpaRepository<JobAlert, Long> {
  List<JobAlert> findByUserOrderByCreatedAtDesc(User user);
}
