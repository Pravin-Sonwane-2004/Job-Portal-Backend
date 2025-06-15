package com.pravin.job_portal_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "saved_job", indexes = {
    @Index(name = "idx_savedjob_user", columnList = "user_id"),
    @Index(name = "idx_savedjob_job", columnList = "job_id")
})
@ToString(exclude = { "user", "job" })
public final class SavedJob {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_savedjob_user"))
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "job_id", nullable = false, foreignKey = @ForeignKey(name = "fk_savedjob_job"))
  private Job job;

  @Column(name = "saved_at", nullable = false, updatable = false)
  @org.hibernate.annotations.CreationTimestamp
  private LocalDateTime savedAt;

  @Column(name = "updated_at")
  @org.hibernate.annotations.UpdateTimestamp
  private LocalDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    SavedJob that = (SavedJob) o;
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
