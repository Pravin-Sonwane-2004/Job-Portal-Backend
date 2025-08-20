package com.pravin.job_portal_backend.mapper.job_mapper;

import com.pravin.job_portal_backend.dto.job_dtos.JobSummaryDTO;
import com.pravin.job_portal_backend.entity.Job;

public class JobSummaryMapper {

    // ✅ Entity → DTO
    public static JobSummaryDTO toDto(Job job) {
        if (job == null)
            return null;

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

    // ⚠️ Usually we don’t need DTO → Entity for a *summary*, but if required:
    public static Job toEntity(JobSummaryDTO dto) {
        if (dto == null)
            return null;

        Job job = new Job();
        job.setId(dto.getId());
        job.setTitle(dto.getTitle());
        job.setLocation(dto.getLocation());
        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());
        job.setCategory(dto.getCategory());
        // Company mapping skipped (handled elsewhere)
        return job;
    }
}
