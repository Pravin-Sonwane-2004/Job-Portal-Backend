package com.pravin.job_portal_backend.dto.apply_job_dtos;

import lombok.*;

import java.time.LocalDateTime;

import com.pravin.job_portal_backend.entity.ApplyJob.ApplyJobBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builderpublic Object id(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'id'");
  }
public class ApplyJobResponseDTO {
    private Long applicationId;
    private String jobTitle;
    private String company;
    private String userEmail;
    private LocalDateTime appliedAt;
    private String status;
    private String resumeLink;
    private String coverLetter;
    private String jobLocation;
    private Double jobSalary;
    public static ApplyJobBuilder builder() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'builder'");
    }
}
