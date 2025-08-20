package com.pravin.job_portal_backend.mapper.job_mapper;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.Job;

public class JobMapper {

    // Convert DTO -> Entity (for create)
    public static Job toEntity(JobRequestDTO dto, Company company) {
        if (dto == null) {
            return null;
        }
        return Job.builder()
                .title(dto.getTitle())
                .location(dto.getLocation())
                .minSalary(dto.getMinSalary())
                .maxSalary(dto.getMaxSalary())
                .description(dto.getDescription())
                .requirements(dto.getRequirements())
                .jobType(dto.getJobType())
                .experienceLevel(dto.getExperienceLevel())
                .category(dto.getCategory())
                .lastDateToApply(dto.getLastDateToApply())
                .company(company)
                .build();
    }

    // Convert Entity -> Response DTO (for detailed job view)
    public static JobResponseDTO toResponse(Job job) {
        if (job == null) {
            return null;
        }
        return JobResponseDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .status(job.getJobStatus())
                .category(job.getCategory())
                .postedDate(job.getPostedDate())
                .lastDateToApply(job.getLastDateToApply())
                .postedDaysAgo(job.postedDaysAgo())
                .companyId(job.getCompany() != null ? job.getCompany().getId() : null)
                .build();
    }

    // Convert Entity -> Summary DTO (for job listings/search results)
    public static JobSummaryDTO toSummary(Job job) {
        if (job == null) {
            return null;
        }
        return JobSummaryDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .category(job.getCategory())
                .companyName(job.getCompany() != null ? job.getCompany().getName() : null)
                .build();
    }

    // Update existing Entity with DTO values (for edit/update)
    public static void updateEntity(Job job, JobRequestDTO dto, Company company) {
        if (job == null || dto == null) {
            return;
        }
        job.setTitle(dto.getTitle());
        job.setLocation(dto.getLocation());
        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());
        job.setDescription(dto.getDescription());
        job.setRequirements(dto.getRequirements());
        job.setJobType(dto.getJobType());
        job.setExperienceLevel(dto.getExperienceLevel());
        job.setCategory(dto.getCategory());
        job.setLastDateToApply(dto.getLastDateToApply());
        job.setCompany(company);
    }
}
