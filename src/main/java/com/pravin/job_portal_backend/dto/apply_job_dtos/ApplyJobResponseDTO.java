package com.pravin.job_portal_backend.dto.apply_job_dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobResponseDTO {
    private Long applicationId;
    private String jobTitle;
    private String company;
    private String userEmail;
    private LocalDateTime appliedAt;
    private String status;
    private String resumeLink;
    private String coverLetter;
    private String jobLocation;
    private Double jobSalary;
}
