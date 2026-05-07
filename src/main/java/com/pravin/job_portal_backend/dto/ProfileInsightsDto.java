package com.pravin.job_portal_backend.dto;

import com.pravin.job_portal_backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileInsightsDto {
    private Role role;
    private long totalUsers;
    private long totalJobs;
    private long totalApplications;
    private long savedJobs;
    private long resumes;
    private int profileCompletion;
    private String nextAction;
}
