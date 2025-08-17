package com.pravin.job_portal_backend.dto.apply_job_dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationProfileDtoAdmin {
    private Long applicationId;
    private String jobTitle;
    private String company;
    private String applicantName;
    private String applicantEmail;
    private String applicantProfile; // bio or summary
    private String status;
    private String resumeLink;
    private String coverLetter;
    private String source;
}
