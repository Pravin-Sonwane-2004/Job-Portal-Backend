package com.pravin.job_portal_backend.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String website;
    private String location;
    private String industry;
    private String contactEmail;

    @Version
    private Long version; // ✅ added for optimistic locking

    // One company -> Many jobs
    @OneToMany(mappedBy = "company", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = false)
    private List<Job> jobs;

    // One company -> Many recruiters
    @OneToMany(mappedBy = "company", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = false)
    private List<Recruiter> recruiters;

}
