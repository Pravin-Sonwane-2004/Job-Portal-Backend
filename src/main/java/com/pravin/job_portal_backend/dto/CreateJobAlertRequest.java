package com.pravin.job_portal_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateJobAlertRequest(
    @NotBlank String keywords,
    String location) {
}
