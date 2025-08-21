package com.pravin.job_portal_backend.dto.job_dtos;

import lombok.*;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSummaryDTO {
    private Long id;
    private String title;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private String category;
    private String companyName;
}
