package com.pravin.job_portal_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a job application by a user.
 * Each record links a user to a job they have applied for.
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
@ToString(exclude = { "user", "job" })
public final class ApplyJob {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_applyjob_user"))
  private User user;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "job_id", nullable = false, foreignKey = @ForeignKey(name = "fk_applyjob_job"))
  private Job job;

  @CreationTimestamp
  @Column(name = "applied_at", nullable = false, updatable = false)
  private LocalDateTime appliedAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "status", length = 50, nullable = false)
  private String status; // e.g., APPLIED, UNDER_REVIEW, REJECTED, HIRED

  @Column(name = "resume_link", length = 500)
  private String resumeLink;

  @Column(name = "cover_letter", columnDefinition = "TEXT")
  private String coverLetter;

  @Column(name = "applied_from_ip", length = 45)
  private String appliedFromIp;

  @Column(name = "source", length = 100)
  private String source; // e.g., Web, Mobile, Referral

  @Column(name = "user_agent", length = 512)
  private String userAgent;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ApplyJob other))
      return false;
    return user != null && job != null &&
        user.equals(other.user) &&
        job.equals(other.job);
  }

  @Override
  public int hashCode() {
    int result = user != null ? user.hashCode() : 0;
    result = 31 * result + (job != null ? job.hashCode() : 0);
    return result;
  }
}
