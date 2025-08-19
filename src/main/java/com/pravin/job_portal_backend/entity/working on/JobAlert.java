// package com.pravin.job_portal_backend.entity;

// import java.time.LocalDateTime;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Table(name = "job_alerts")
// public class JobAlert {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "JOB_SEEKER_id", nullable = false)
//     private JOB_SEEKER JOB_SEEKER;

//     @Column(nullable = false)
//     private String criteria; // e.g. JSON or comma-separated string for keywords, location, etc.

//     @Column(name = "created_at", nullable = false)
//     private LocalDateTime createdAt = LocalDateTime.now();

// }
