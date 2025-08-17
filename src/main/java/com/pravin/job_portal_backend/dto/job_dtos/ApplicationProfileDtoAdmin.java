package com.pravin.job_portal_backend.dto.job_dtos;

import lombok.*;

import java.time.LocalDateTime;

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
  private String applicantProfile;
  private String status; // Application status (APPLIED, REVIEWED, etc.)
  private LocalDateTime appliedAt;
  private String resumeLink;
  private String source; // e.g., 'LinkedIn', 'Referral', 'Job Portal'
  private String coverLetter;
  private String reviewerRemarks; // Notes by recruiter/admin
}
