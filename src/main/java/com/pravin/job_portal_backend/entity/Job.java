package com.pravin.job_portal_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Entity representing a job posting.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "job", indexes = {
    @Index(name = "idx_job_title", columnList = "title"),
    @Index(name = "idx_job_location", columnList = "location")
})
@ToString(exclude = {"applications"})
public final class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "salary", nullable = false, length = 100)
    private String salary;

    @Column(name = "company", nullable = false, length = 255)
    private String company;

    @Column(name = "posted_date", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDate postedDate;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplyJob> applications = new java.util.ArrayList<>();

    public long postedDaysAgo() {
        if (postedDate == null) {
            return -1;
        }
        return ChronoUnit.DAYS.between(postedDate, LocalDate.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return id != null && id.equals(job.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
