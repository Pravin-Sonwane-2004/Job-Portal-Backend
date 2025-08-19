package com.pravin.job_portal_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private JobApplication job;

    @ManyToOne
    private JobApplication jobSeeker;

    private String status; // e.g., APPLIED, REVIEWED, REJECTED, HIRED

    private LocalDateTime appliedAt;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobApplication getJob() {
        return job;
    }

    public void setJob(JobApplication job2) {
        this.job = job2;
    }

    public JobApplication getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(JobApplication JOB_SEEKER) {
        this.jobSeeker = JOB_SEEKER;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
}
