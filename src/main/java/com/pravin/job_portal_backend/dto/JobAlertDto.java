package com.pravin.job_portal_backend.dto;

import java.time.LocalDateTime;

public record JobAlertDto(
    Long id,
    String keywords,
    String location,
    boolean active,
    LocalDateTime createdAt) {
}
