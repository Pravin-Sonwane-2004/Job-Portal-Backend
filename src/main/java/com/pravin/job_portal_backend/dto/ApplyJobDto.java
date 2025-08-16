package com.pravin.job_portal_backend.dto;

import com.pravin.job_portal_backend.dto.user_dtos.UserDetialsDto;
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

  private String appliedFromIp;
  private String source;
  private String userAgent;

    private UserDetialsDto user; // Full applicant info admin
  private JobDto job; // Full job info
}
