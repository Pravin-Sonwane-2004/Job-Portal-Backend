package com.pravin.job_portal_backend.dto.job_dtos;

import com.pravin.job_portal_backend.enums.ExperienceLevel;
import com.pravin.job_portal_backend.enums.JobStatus;
import com.pravin.job_portal_backend.enums.JobType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

// ✅ For creating/updating job
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequestDTO {
    private String title;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private String description;
    private List<String> requirements;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private String category;
    private LocalDate lastDateToApply;
    private Long companyId;
}
