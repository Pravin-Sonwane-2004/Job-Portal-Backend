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
//     private User employer;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "user_id", nullable = false)
//     private User user;

//     @Column(name = "scheduled_time", nullable = false)
//     private LocalDateTime scheduledTime;

//     @Column(nullable = false)
//     private String status; // e.g. SCHEDULED, RESCHEDULED, COMPLETED, CANCELLED

//     public Interview() {}

//     public Interview(ApplyJob application, User employer, User user, LocalDateTime scheduledTime, String status) {
//         this.application = application;
//         this.employer = employer;
//         this.user = user;
//         this.scheduledTime = scheduledTime;
//         this.status = status;
//     }

//     public Long getId() { return id; }
//     public ApplyJob getApplication() { return application; }
//     public User getEmployer() { return employer; }
//     public User getUser() { return user; }
//     public LocalDateTime getScheduledTime() { return scheduledTime; }
//     public String getStatus() { return status; }

//     public void setId(Long id) { this.id = id; }
//     public void setApplication(ApplyJob application) { this.application = application; }
//     public void setEmployer(User employer) { this.employer = employer; }
//     public void setUser(User user) { this.user = user; }
//     public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
//     public void setStatus(String status) { this.status = status; }
// }
