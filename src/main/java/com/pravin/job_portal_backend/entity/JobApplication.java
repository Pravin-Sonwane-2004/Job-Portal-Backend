package com.pravin.job_portal_backend.entity;

import com.pravin.job_portal_backend.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "job_application", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_job_application", columnNames = { "user_id", "job_id" })
})
public class JobApplication implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // === Primary Key ===
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // === Job Reference ===
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false, foreignKey = @ForeignKey(name = "fk_job_application_job"))
    private Job job;

    // === User (Applicant) Reference ===
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_job_application_user"))
    private User user;

    // === Application Status ===
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    // === Timestamp ===
    @Column(name = "applied_at", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime appliedAt;

    @Column(name = "updated_at")
    @org.hibernate.annotations.UpdateTimestamp
    private LocalDateTime updatedAt;
}
