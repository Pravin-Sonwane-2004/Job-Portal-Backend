package com.pravin.job_portal_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pravin.job_portal_backend.enums.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(exclude = { "applications", "postedBy", "company" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "job", indexes = {
        @Index(name = "idx_job_title", columnList = "title"),
        @Index(name = "idx_job_location", columnList = "location"),
        @Index(name = "idx_job_category", columnList = "category")
        // @Index(name = "idx_job_status", columnList = "status")
})
public class Job implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // === Primary key ===
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    // === Job title ===
    @NotBlank
    @Size(max = 255)
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    // === Job location ===
    @NotBlank
    @Size(max = 255)
    @Column(name = "location", nullable = false, length = 255)
    private String location;

    // === Salary details ===
    @NotNull
    @Column(name = "min_salary", nullable = false)
    private Double minSalary;

    @NotNull
    @Column(name = "max_salary", nullable = false)
    private Double maxSalary;

    // === Linked Company ===
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "fk_job_company"))
    @JsonIgnore
    private Company company;

    // === Job description ===
    @Lob
    @NotBlank
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    // === Requirements list (skills/experience expected) ===
    @ElementCollection
    @CollectionTable(name = "job_requirements", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "requirement")
    private List<String> requirements = new ArrayList<>();

    // === Job Type Enum ===
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    // === Experience Level Enum ===
    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", nullable = false)
    private ExperienceLevel experienceLevel;

    // === Job Status Enum ===
    @Enumerated(EnumType.STRING)
    // @Column(name = "status", nullable = false)
    private JobStatus JobStatus;

    // === Job category ===
    @Column(name = "category", length = 100)
    private String category;

    // === Who posted the job (Recruiter / Admin) ===
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", foreignKey = @ForeignKey(name = "fk_job_posted_by"))
    @JsonIgnore
    private User postedBy;

    // === Auto-generated creation time ===
    @Column(name = "posted_date", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDate postedDate;

    // === Last date to apply ===
    @Column(name = "last_date_to_apply")
    private LocalDate lastDateToApply;

    // === Timestamp of last update ===
    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // === All applications to this job ===
    @JsonIgnore
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> applications = new ArrayList<>();

    // === Soft delete flag ===
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    // === Helper methods ===

    // Calculate how many days ago it was posted
    public long postedDaysAgo() {
        if (postedDate == null) {
            return -1;
        }
        return ChronoUnit.DAYS.between(postedDate, LocalDate.now());
    }

    // Set default last date to apply (30 days from now if not provided)
    @PrePersist
    public void setDefaultLastDateToApply() {
        if (this.lastDateToApply == null) {
            this.lastDateToApply = LocalDate.now().plusDays(30);
        }
    }
}
