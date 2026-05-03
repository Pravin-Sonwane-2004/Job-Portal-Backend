package com.pravin.job_portal_backend.dto;

import com.pravin.job_portal_backend.enums.Role;

public record ProfileInsightsDto(
    Role role,
    long totalUsers,
    long totalJobs,
    long totalApplications,
    long savedJobs,
    long resumes,
    int profileCompletion,
    String nextAction) {
}
