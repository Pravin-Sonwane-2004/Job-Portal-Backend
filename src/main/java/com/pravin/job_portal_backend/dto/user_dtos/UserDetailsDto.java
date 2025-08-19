package com.pravin.job_portal_backend.dto.user_dtos;

import com.pravin.job_portal_backend.enums.ExperienceLevel;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for User entity, excluding sensitive fields like password and reset token.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDto {
    private Long id;
    private String email;
    private String username;
    private String name;
    private String avatarUrl;
    private String designation;
    private Boolean verified;
    private String location;
    private String bio;
    private String phoneNumber;
    private String linkedinUrl;
    private String githubUrl;
    private boolean blocked;
    private Role role;
    private ExperienceLevel experienceLevel;
    private UserStatus status;
    private List<String> skills;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
