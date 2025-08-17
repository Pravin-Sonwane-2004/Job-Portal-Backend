package com.pravin.job_portal_backend.dto.job_dtos;


import com.pravin.job_portal_backend.enums.ExperienceLevel;
import com.pravin.job_portal_backend.enums.JobStatus;
import com.pravin.job_portal_backend.enums.JobType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAdminDTO {

    private Long id;
    private String title;
    private String company;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private String currency;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private JobStatus status;
    private String category;
    private LocalDate postedDate;
    private LocalDate lastDateToApply;
    private boolean deleted;
    private Long postedByUserId;
    private int applicationsCount;
}
