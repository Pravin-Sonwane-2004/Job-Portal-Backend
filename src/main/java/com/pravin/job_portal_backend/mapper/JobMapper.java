package com.pravin.job_portal_backend.mapper;

import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.entity.Job;

public class JobMapper {
    public static JobDto toDto(Job job) {
        if (job == null) return null;
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .location(job.getLocation())
                .salary(job.getSalary())
                .company(job.getCompany())
                .postedDate(job.getPostedDate())
                .postedDaysAgo(job.postedDaysAgo())
                .build();
    }

    public static Job toEntity(JobDto dto) {
        if (dto == null) return null;
        Job job = new Job();
        job.setId(dto.getId());
        job.setTitle(dto.getTitle());
        job.setLocation(dto.getLocation());
        job.setSalary(dto.getSalary());
        job.setCompany(dto.getCompany());
        // Do not set postedDate from DTO; let DB handle it
        return job;
    }
}
