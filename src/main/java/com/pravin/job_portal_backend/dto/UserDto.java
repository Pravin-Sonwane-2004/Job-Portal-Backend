package com.pravin.job_portal_backend.dto;

import lombok.*;
import java.util.List;

import com.pravin.job_portal_backend.enums.ExperienceLevel;

/**
 * DTO for User entity, excluding sensitive fields like password.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String avatarUrl;
    private String designation;
    private Boolean verified;
    private String location;
    private String bio;
    private String phoneNumber;
    private String linkedinUrl;
    private String jobRole;
    private String role;
    private ExperienceLevel experienceLevel;
    private List<String> roles; // <-- Add this line
    private List<String> skills;
    private Long companyId;
    private String companyName;
}
