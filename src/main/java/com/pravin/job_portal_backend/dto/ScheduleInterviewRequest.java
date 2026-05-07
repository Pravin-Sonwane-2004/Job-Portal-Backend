package com.pravin.job_portal_backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInterviewRequest {
    @NotNull
    private Long applicationId;

    @NotNull
    private LocalDateTime scheduledTime;

    private String meetingLink;
    private String notes;
}
