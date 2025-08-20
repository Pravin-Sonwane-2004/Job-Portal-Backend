package com.pravin.job_portal_backend.dto.job_application_dtos;

import com.pravin.job_portal_backend.enums.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobResponseDTO {
    private Long applicationId;
    private Long jobId;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
}
