package com.pravin.job_portal_backend.dto.user_dtos;

import com.pravin.job_portal_backend.enums.Role;
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
//    private String jobRole;
    private Role role;
    private ExperienceLevel experienceLevel;
    private List<String> skills;
}
