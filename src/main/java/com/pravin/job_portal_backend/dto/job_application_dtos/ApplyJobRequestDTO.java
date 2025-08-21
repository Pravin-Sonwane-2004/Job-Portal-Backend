package com.pravin.job_portal_backend.dto.job_application_dtos;

import lombok.*;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobRequestDTO {
    private Long jobId;
    private Long userId; // Optional if you get from JWT auth
    private String resumeUrl; // If resumes are uploaded
}
