package com.pravin.job_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJobRequestDTO {

  private Long userId;
  private Long jobId;
  private String resumeLink;
  private String coverLetter;
  private String appliedFromIp;
}
