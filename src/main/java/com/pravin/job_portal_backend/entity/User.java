package com.pravin.job_portal_backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pravin.job_portal_backend.enums.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Entity representing a user in the job portal system.
 * Includes authentication, profile, and status information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_role", columnList = "role")
})
@ToString(exclude = { "appliedJobs", "skills" })
public final class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User's unique email address */
    @Email
    @NotBlank
    @Size(max = 255)
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /** User's hashed password */
    @NotBlank
    @Size(min = 8, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    // /** Unique username for login */
    // @NotBlank
    // @Size(max = 255)
    // @Column(name = "username", unique = true, nullable = false)
    // private String username;

    /** Full name */
    @Size(max = 255)
    @NotBlank(message = "Name is required")
    @Column(nullable = true)
    private String name;

    /** User's role in the system */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private Role role;

    /** User's experience level */
    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level")
    private ExperienceLevel experienceLevel;

    /** User's account status */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    /** Avatar image URL */
    @Size(max = 512)
    @Column(name = "avatar_url")
    private String avatarUrl;

    /** Job designation/title */
    @Size(max = 255)
    private String designation;

    /** Email verification status */
    @Column(name = "verified")
    private Boolean verified;

    /** Location/city */
    @Size(max = 255)
    private String location;

    /** Short bio */
    @Size(max = 1024)
    private String bio;

    /** Phone number */
    @Size(max = 20)
    @Pattern(regexp = "^[0-9+\\-() ]*$", message = "Invalid phone number format")
    @Column(name = "phone_number")
    private String phoneNumber;

    /** LinkedIn profile URL */
    @Size(max = 512)
    @Column(name = "linkedin_url")
    private String linkedinUrl;

    /** GitHub profile URL */
    @Size(max = 512)
    @Column(name = "github_url")
    private String githubUrl;

    /** Blocked status */
    @Column(nullable = false)
    @Builder.Default
    private boolean blocked = false;

    /** Soft delete flag */
    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    /** Jobs applied by the user */
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // @Builder.Default
    // private List<ApplyJob> appliedJobs = new ArrayList<>();

    /** User's skills */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill", length = 100)
    @Builder.Default
    private List<String> skills = new ArrayList<>();

    /** Account creation timestamp */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Last update timestamp */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Password reset token */
    @Column(name = "reset_token")
    private String resetToken;

    /** Token expiry timestamp */
    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    /** Set timestamps and defaults before persisting */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null)
            this.status = UserStatus.ACTIVE;
        if (this.verified == null)
            this.verified = false;
        if (this.isDeleted == null)
            this.isDeleted = false;
    }

    /** Update timestamp before updating */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
