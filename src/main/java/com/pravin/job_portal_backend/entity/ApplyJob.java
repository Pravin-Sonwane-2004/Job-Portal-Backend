package com.pravin.job_portal_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a job application by a user.
 * Maps to the apply_job table.
 */
@Entity
@Table(name = "apply_job", indexes = {
    @Index(name = "idx_applyjob_user", columnList = "user_id"),
    @Index(name = "idx_applyjob_job", columnList = "job_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "job"})
public final class ApplyJob {

  /**
     * Primary key for ApplyJob.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * The user who applied for the job.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_applyjob_user"))
    private User user;

    /**
     * The job that was applied to.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false, foreignKey = @ForeignKey(name = "fk_applyjob_job"))
    private Job job;

    /**
     * Timestamp when the application was made.
     */
    @Column(name = "applied_at", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime appliedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplyJob that = (ApplyJob) o;
        return user != null && job != null &&
                user.equals(that.user) &&
                job.equals(that.job);
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (job != null ? job.hashCode() : 0);
        return result;
    }
}
