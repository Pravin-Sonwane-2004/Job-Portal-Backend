package com.pravin.job_portal_backend.repository;

import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.SavedJob;
import com.pravin.job_portal_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByUser(User user);
    long countByUser(User user);
    Optional<SavedJob> findByUserAndJob(User user, Job job);
    void deleteByUserAndJob(User user, Job job);
}
