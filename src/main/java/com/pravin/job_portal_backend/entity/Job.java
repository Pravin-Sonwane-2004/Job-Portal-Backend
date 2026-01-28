package com.pravin.job_portal_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pravin.job_portal_backend.enums.ExperienceLevel;
import com.pravin.job_portal_backend.enums.JobStatus;
import com.pravin.job_portal_backend.enums.JobType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Getter
@Setter
@Builder
@ToString(exclude = { "applications", "postedBy" })
@Entity
@Table(name = "job", indexes = {
    @Index(name = "idx_job_title", columnList = "title"),
    @Index(name = "idx_job_location", columnList = "location")
})
public class Job implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

  // Primary key
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  // Job title
  @NotBlank
  @Size(max = 255)
  @Column(name = "title", nullable = false, length = 255)
  private String title;

  // Job location
  @NotBlank
  @Size(max = 255)
  @Column(name = "location", nullable = false, length = 255)
  private String location;

  // Salary info
  @NotBlank
  @Size(max = 100)
  @Column(name = "salary", nullable = false, length = 100)
  private String salary;

  // Company name
  @NotBlank
  @Size(max = 255)
  @Column(name = "company", nullable = true, length = 255)
  private String company;

  // Job description
  @Lob
  @Column(name = "description", nullable = true, columnDefinition = "TEXT")
  private String description;

  // Requirements list (skills/experience expected)
  @ElementCollection
  @CollectionTable(name = "job_requirements", joinColumns = @JoinColumn(name = "job_id"))
  @Column(name = "requirement")
  private List<String> requirements = new ArrayList<>();

  // Job Type Enum
  @Enumerated(EnumType.STRING)
  @Column(name = "job_type", nullable = true)
  private JobType jobType;

  // Experience Level Enum
  @Enumerated(EnumType.STRING)
  @Column(name = "experience_level", nullable = true)
  private ExperienceLevel experienceLevel;

  // Job Status Enum
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = true)
  private JobStatus status = JobStatus.OPEN;

  // Job category
  @Column(name = "category", length = 100)
  private String category;

  // Who posted the job
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "posted_by", foreignKey = @ForeignKey(name = "fk_job_posted_by"))
  private User postedBy;

  // Auto-generated creation time
  @Column(name = "posted_date", nullable = true, updatable = false)
  @org.hibernate.annotations.CreationTimestamp
  private LocalDate postedDate;

  // Last date to apply
  @Column(name = "last_date_to_apply")
  private LocalDate lastDateToApply;

  // Timestamp of last update
  @org.hibernate.annotations.UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // All applications to this job
  @JsonIgnore
  @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ApplyJob> applications = new ArrayList<>();

  // Calculate how many days ago it was posted
  public long postedDaysAgo() {
    if (postedDate == null) {
      return -1;
    }
    return ChronoUnit.DAYS.between(postedDate, LocalDate.now());
  }
}
