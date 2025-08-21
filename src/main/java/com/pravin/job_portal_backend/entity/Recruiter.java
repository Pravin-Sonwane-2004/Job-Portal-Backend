package com.pravin.job_portal_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One recruiter <-> One user (login account)
    @OneToOne
    @JoinColumn(name = "user_id", unique = true) // FK column in recruiter table
    private User user;

    // Many recruiters -> One company
    @ManyToOne
    @JoinColumn(name = "company_id") // FK column in recruiter table
    private Company company;
}
