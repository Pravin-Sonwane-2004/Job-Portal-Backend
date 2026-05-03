package com.pravin.job_portal_backend.dto;

import java.time.LocalDateTime;

public record InterviewDto(
    Long id,
    Long applicationId,
    Long candidateId,
    String candidateName,
    Long employerId,
    String employerName,
    LocalDateTime scheduledTime,
    String status,
    String meetingLink,
    String notes) {
}
