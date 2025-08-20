package com.pravin.job_portal_backend.dto.job_application_dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobSummaryDTO {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String status;
}
