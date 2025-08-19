// package com.pravin.job_portal_backend.entity;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "interviews")
// public class Interview {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "application_id", nullable = false)
//     private ApplyJob application;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "employer_id", nullable = false)
//     private JOB_SEEKER employer;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "JOB_SEEKER_id", nullable = false)
//     private JOB_SEEKER JOB_SEEKER;

//     @Column(name = "scheduled_time", nullable = false)
//     private LocalDateTime scheduledTime;

//     @Column(nullable = false)
//     private String status; // e.g. SCHEDULED, RESCHEDULED, COMPLETED, CANCELLED

//     public Interview() {}

//     public Interview(ApplyJob application, JOB_SEEKER employer, JOB_SEEKER JOB_SEEKER, LocalDateTime scheduledTime, String status) {
//         this.application = application;
//         this.employer = employer;
//         this.JOB_SEEKER = JOB_SEEKER;
//         this.scheduledTime = scheduledTime;
//         this.status = status;
//     }

//     public Long getId() { return id; }
//     public ApplyJob getApplication() { return application; }
//     public JOB_SEEKER getEmployer() { return employer; }
//     public JOB_SEEKER getJOB_SEEKER() { return JOB_SEEKER; }
//     public LocalDateTime getScheduledTime() { return scheduledTime; }
//     public String getStatus() { return status; }

//     public void setId(Long id) { this.id = id; }
//     public void setApplication(ApplyJob application) { this.application = application; }
//     public void setEmployer(JOB_SEEKER employer) { this.employer = employer; }
//     public void setJOB_SEEKER(JOB_SEEKER JOB_SEEKER) { this.JOB_SEEKER = JOB_SEEKER; }
//     public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
//     public void setStatus(String status) { this.status = status; }
// }
