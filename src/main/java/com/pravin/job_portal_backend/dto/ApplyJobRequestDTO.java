package com.pravin.job_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body used when a user applies for a job.
 *
 * Notice that userId and jobId are not trusted from this body:
 * userId comes from the logged-in JWT user, and jobId comes from the request
 * parameter/path handled by the controller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJobRequestDTO {

  // Candidate-provided application details.
  private String resumeLink;
  private String coverLetter;
  private String phoneNumber;
  private String linkedinUrl;
  private String portfolioUrl;
  private String expectedSalary;
  private String noticePeriod;

  // Optional audit/source data for tracking where the application came from.
  private String appliedFromIp;
  private String source;
  private String userAgent;
}
