package com.pravin.job_portal_backend.dto.job_dtos;

import com.pravin.job_portal_backend.enums.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationResponseDTO {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private Long userId;
    private String userEmail;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
