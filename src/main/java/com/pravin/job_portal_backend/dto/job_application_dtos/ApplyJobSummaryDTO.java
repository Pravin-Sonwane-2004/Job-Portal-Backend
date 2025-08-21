package com.pravin.job_portal_backend.dto.job_application_dtos;

import com.pravin.job_portal_backend.enums.ApplicationStatus;
import lombok.*;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobSummaryDTO {
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private ApplicationStatus status;
}
