package com.pravin.job_portal_backend.dto.user_dtos;

import com.pravin.job_portal_backend.enums.ExperienceLevel;
import com.pravin.job_portal_backend.enums.Role;
import lombok.*;

import java.util.List;

/**
 * DTO for User entity, excluding sensitive fields like password.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetialsDto {
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
    private String githubURL;
    private boolean blocked;
    //    private String jobRole;
    private Role role;
    private ExperienceLevel experienceLevel;
    private List<String> skills;
}
