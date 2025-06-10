package com.pravin.job_portal_backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJobResponseDTO {

  private Long applicationId;
  private String jobTitle;
  private String company;
  private String userEmail;
  private LocalDateTime appliedAt;
  private String status;
  private String recruiterRemarks;
  private java.time.LocalDate postedDate;
  private String location;
  private String salary;
}
