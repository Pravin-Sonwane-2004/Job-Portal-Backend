// package com.pravin.job_portal_backend.entity;

// import jakarta.persistence.*;
// import java.util.List;

// @Entity
// @Table(name = "companies")
// public class Company {
//   @Id
//   @GeneratedValue(strategy = GenerationType.IDENTITY)
//   private Long id;

//   @Column(nullable = false, unique = true)
//   private String name;

//   @Column(length = 2000)
//   private String description;

//   @Column
//   private String website;

//   // @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
//   // private List<CompanyReview> reviews;
// }
