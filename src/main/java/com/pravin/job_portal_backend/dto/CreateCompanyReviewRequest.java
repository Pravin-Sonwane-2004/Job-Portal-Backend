package com.pravin.job_portal_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateCompanyReviewRequest(
    @NotBlank String content,
    @Min(1) @Max(5) int rating) {
}
