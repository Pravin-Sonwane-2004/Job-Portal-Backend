package com.pravin.job_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for handling job application requests from users.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJobRequestDTO {

  private Long userId;
  private Long jobId;

  private String resumeLink; // URL to uploaded resume
  private String coverLetter; // Optional text or attachment reference
  private String appliedFromIp; // IP address for auditing or security

  private String status; // e.g., APPLIED, UNDER_REVIEW, etc. (optional default = APPLIED)
  private LocalDateTime appliedAt; // Can be set automatically by backend, but optional if needed in request
  private String source; // Source of application (e.g., "Web", "Mobile", "Referral")

  // Optional: device info or browser fingerprint for advanced logging
  private String userAgent; // e.g., Mozilla/5.0...
}
