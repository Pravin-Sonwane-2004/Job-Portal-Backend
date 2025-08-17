package com.pravin.job_portal_backend.dto.job_dtos;


import com.pravin.job_portal_backend.enums.JobStatus;
import com.pravin.job_portal_backend.enums.JobType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSummaryDTO {

    private Long id;
    private String title;
    private String company;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private String currency;
    private JobType jobType;
    private String category;
    private JobStatus status;
    private long postedDaysAgo;
}
