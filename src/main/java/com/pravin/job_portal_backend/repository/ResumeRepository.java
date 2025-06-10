package com.pravin.job_portal_backend.repository;

import com.pravin.job_portal_backend.entity.Resume;
import com.pravin.job_portal_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUser(User user);
}
