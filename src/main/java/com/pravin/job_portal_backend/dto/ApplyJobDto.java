package com.pravin.job_portal_backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobDto {
    private Long id;
    private Long userId;
    private Long jobId;
    private LocalDateTime appliedAt;
    private UserDto user; // Full applicant info
    private JobDto job;   // Full job info (optional, but useful for admin)
}
