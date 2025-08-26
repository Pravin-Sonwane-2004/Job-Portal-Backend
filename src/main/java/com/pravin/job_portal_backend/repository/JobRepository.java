package com.pravin.job_portal_backend.repository;
import com.pravin.job_portal_backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // Find jobs by company
    List<Job> findByCompanyId(Long companyId);
}
