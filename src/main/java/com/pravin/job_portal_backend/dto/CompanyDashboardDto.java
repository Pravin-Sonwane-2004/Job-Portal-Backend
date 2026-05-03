package com.pravin.job_portal_backend.dto;

public record CompanyDashboardDto(
    CompanyDto company,
    long employees,
    long jobs,
    long applications,
    String nextAction) {
}
