package com.pravin.job_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJobRequestDTO {

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
}
