package com.pravin.job_portal_backend.dto;

import java.time.LocalDateTime;

public record CompanyReviewDto(
    Long id,
    Long companyId,
    String companyName,
    Long userId,
    String userName,
    String content,
    int rating,
    LocalDateTime createdAt) {
}
