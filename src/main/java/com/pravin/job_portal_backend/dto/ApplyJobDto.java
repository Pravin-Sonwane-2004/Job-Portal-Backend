package com.pravin.job_portal_backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobDto {

  private Long id;

  private Long userId;
  private Long jobId;

  private LocalDateTime appliedAt;
  private LocalDateTime updatedAt;

  private String status;
  private String recruiterRemarks;

  private String resumeLink;
  private String coverLetter;
  private String phoneNumber;
  private String linkedinUrl;
  private String portfolioUrl;
  private String expectedSalary;
  private String noticePeriod;

  private String appliedFromIp;
  private String source;
  private String userAgent;

  private UserDto user; // Full applicant info admin
  private JobDto job; // Full job info
}
