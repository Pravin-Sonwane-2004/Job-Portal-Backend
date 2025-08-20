package com.pravin.job_portal_backend.dto.job_application_dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobResponseDTO {
    private Long id;
    private Long jobId;
    private Long userId;
    private String coverLetter;
    private String status; // e.g., "PENDING", "ACCEPTED", "REJECTED"
    private LocalDateTime appliedAt;
}
