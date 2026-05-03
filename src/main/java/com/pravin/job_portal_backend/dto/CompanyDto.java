package com.pravin.job_portal_backend.dto;

public record CompanyDto(
    Long id,
    String name,
    String description,
    String website,
    String industry,
    String location,
    String logoUrl,
    boolean verified,
    long employeeCount,
    long jobCount) {
}
