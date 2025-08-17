package com.pravin.job_portal_backend.dto.apply_job_dtos;



import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobDto {
    private Long applicationId;
    private Long userId;
    private Long jobId;
    private String status;
    private String resumeLink;
    private String coverLetter;
    private String appliedFromIp;
    private String source;
    private String userAgent;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
