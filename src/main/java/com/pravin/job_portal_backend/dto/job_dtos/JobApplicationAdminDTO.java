package com.pravin.job_portal_backend.dto.job_dtos;

import com.pravin.job_portal_backend.enums.ApplicationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationAdminDTO {
    private Long applicationId;
    private String jobTitle;
    private String companyName;
    private String applicantName;
    private String applicantEmail;
    private ApplicationStatus status;
}
