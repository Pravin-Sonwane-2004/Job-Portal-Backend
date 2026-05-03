package com.pravin.job_portal_backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record ScheduleInterviewRequest(
    @NotNull Long applicationId,
    @NotNull LocalDateTime scheduledTime,
    String meetingLink,
    String notes) {
}
