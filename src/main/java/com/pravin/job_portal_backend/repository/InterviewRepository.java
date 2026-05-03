package com.pravin.job_portal_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.ApplyJob;
import com.pravin.job_portal_backend.entity.Interview;
import com.pravin.job_portal_backend.entity.User;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
  List<Interview> findByCandidateOrderByScheduledTimeDesc(User candidate);

  List<Interview> findByEmployerOrderByScheduledTimeDesc(User employer);

  List<Interview> findByApplication(ApplyJob application);
}
