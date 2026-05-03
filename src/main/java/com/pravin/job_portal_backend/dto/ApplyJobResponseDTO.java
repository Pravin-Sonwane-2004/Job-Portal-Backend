package com.pravin.job_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJobResponseDTO {

  private Long applicationId;

  private String jobTitle;
  private String company;
  private String location;
  private String salary;
  private LocalDate postedDate;

  private String userEmail;

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
}
