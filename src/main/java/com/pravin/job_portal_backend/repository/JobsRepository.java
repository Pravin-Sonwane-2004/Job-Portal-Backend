package com.pravin.job_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.Job;

public interface JobsRepository extends JpaRepository<Job, Long> {

}
