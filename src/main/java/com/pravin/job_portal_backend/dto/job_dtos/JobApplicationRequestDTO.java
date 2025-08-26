package com.pravin.job_portal_backend.dto.job_dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationRequestDTO {
    private Long jobId;
    private Long userId;
}
