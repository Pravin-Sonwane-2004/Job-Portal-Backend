package com.pravin.job_portal_backend.dto.job_dtos;

import com.pravin.job_portal_backend.enums.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAdminDTO {

    public JobAdminDTO(Long id2, String title2, String location2, JobStatus status2, Object object) {
        //TODO Auto-generated constructor stub
    }
    private Long id;
    private String title;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private String description;
    private List<String> requirements;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private JobStatus status;
    private String category;
    private String companyName; // ✅ add this field
    private Long postedById; // recruiter/admin id
    private LocalDate postedDate;
    private LocalDate lastDateToApply;
    private boolean deleted;
}
