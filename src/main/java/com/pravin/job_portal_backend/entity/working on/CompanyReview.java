// package com.pravin.job_portal_backend.entity;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "company_reviews")
// public class CompanyReview {
//   @Id
//   @GeneratedValue(strategy = GenerationType.IDENTITY)
//   private Long id;

//   @ManyToOne(fetch = FetchType.LAZY)
//   @JoinColumn(name = "company_id", nullable = false)
//   private Company company;

//   @ManyToOne(fetch = FetchType.LAZY)
//   @JoinColumn(name = "user_id", nullable = false)
//   private User user;

//   @Column(nullable = false, length = 2000)
//   private String content;

//   @Column(nullable = false)
//   private int rating; // 1-5 stars

//   @Column(name = "created_at", nullable = false)
//   private LocalDateTime createdAt = LocalDateTime.now();

// }
