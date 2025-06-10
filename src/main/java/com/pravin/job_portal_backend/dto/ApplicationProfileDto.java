package com.pravin.job_portal_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationProfileDto {
    private Long applicationId;
    private String jobTitle;
    private String company;
    private String applicantName;
    private String applicantEmail;
    private String applicantProfile;
}
