package com.pravin.job_portal_backend.entity;

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
@Table(name = "saved_job", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_job_saved", columnNames = { "user_id", "job_id" })
})
public class SavedJob implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // === Primary Key ===
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // === Job Reference ===
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false, foreignKey = @ForeignKey(name = "fk_saved_job_job"))
    private Job job;

    // === User Reference ===
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_saved_job_user"))
    private User user;

    // === Timestamp ===
    @Column(name = "saved_at", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime savedAt;
}
