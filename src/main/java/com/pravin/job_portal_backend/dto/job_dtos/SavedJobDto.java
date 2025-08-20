package com.pravin.job_portal_backend.dto.job_dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJobDTO {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private LocalDateTime savedAt;
}
