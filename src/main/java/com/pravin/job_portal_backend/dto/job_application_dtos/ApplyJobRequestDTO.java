package com.pravin.job_portal_backend.dto.job_application_dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobRequestDTO {
    private Long jobId;
    private Long userId; // Candidate applying
    private String coverLetter; // Optional
}
