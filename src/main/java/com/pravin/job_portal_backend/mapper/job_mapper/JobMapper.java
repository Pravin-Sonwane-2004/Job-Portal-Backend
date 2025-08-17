package com.pravin.job_portal_backend.mapper.job_mapper;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import com.pravin.job_portal_backend.entity.Job;

import java.util.List;
import java.util.stream.Collectors;

public final class JobMapper {

    private JobMapper() {
    }

    // === Job → JobResponseDTO ===
    public static JobResponseDTO toResponseDto(Job job) {
        if (job == null)
            return null;

        return JobResponseDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .currency(job.getCurrency())
                .company(job.getCompany())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .status(job.getStatus())
                .category(job.getCategory())
                .postedDate(job.getPostedDate())
                .lastDateToApply(job.getLastDateToApply())
                .postedDaysAgo(job.postedDaysAgo())
                .build();
    }

    // === Job → JobSummaryDTO (lightweight for listings) ===
    public static JobSummaryDTO toSummary(Job job) {
        if (job == null)
            return null;

        return JobSummaryDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(job.getCompany())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .currency(job.getCurrency())
                .jobType(job.getJobType())
                .category(job.getCategory())
                .status(job.getStatus())
                .postedDaysAgo(job.postedDaysAgo())
                .build();
    }

    // Alias for service: toSummaryDto
    public static JobSummaryDTO toSummaryDto(Job job) {
        return toSummary(job);
    }

    // === Job → JobAdminDTO (for admin APIs) ===
    public static JobAdminDTO toAdmin(Job job) {
        if (job == null)
            return null;

        return JobAdminDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(job.getCompany())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .currency(job.getCurrency())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .status(job.getStatus())
                .category(job.getCategory())
                .postedDate(job.getPostedDate())
                .lastDateToApply(job.getLastDateToApply())
                .deleted(job.isDeleted())
                .postedByUserId(job.getPostedBy() != null ? job.getPostedBy().getId() : null)
                .applicationsCount(job.getApplications() != null ? job.getApplications().size() : 0)
                .build();
    }

    // Alias for service: toAdminDto
    public static JobAdminDTO toAdminDto(Job job) {
        return toAdmin(job);
    }

    // === JobRequestDTO → Job (Create Entity) ===
    public static Job toEntity(JobRequestDTO dto) {
        if (dto == null)
            return null;

        return Job.builder()
                .title(dto.getTitle())
                .location(dto.getLocation())
                .minSalary(dto.getMinSalary())
                .maxSalary(dto.getMaxSalary())
                .currency(dto.getCurrency())
                .company(dto.getCompany())
                .description(dto.getDescription())
                .requirements(dto.getRequirements())
                .jobType(dto.getJobType())
                .experienceLevel(dto.getExperienceLevel())
                .category(dto.getCategory())
                .lastDateToApply(dto.getLastDateToApply())
                .build();
    }

    // === Update existing Job entity from JobRequestDTO ===
    public static void updateEntityFromDto(JobRequestDTO dto, Job job) {
        if (dto == null || job == null)
            return;

        job.setTitle(dto.getTitle());
        job.setLocation(dto.getLocation());
        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());
        job.setCurrency(dto.getCurrency());
        job.setCompany(dto.getCompany());
        job.setDescription(dto.getDescription());
        job.setRequirements(dto.getRequirements());
        job.setJobType(dto.getJobType());
        job.setExperienceLevel(dto.getExperienceLevel());
        job.setCategory(dto.getCategory());
        job.setLastDateToApply(dto.getLastDateToApply());
    }

    // === List helpers ===
    public static List<JobSummaryDTO> toSummaryList(List<Job> jobs) {
        return jobs.stream().map(JobMapper::toSummary).collect(Collectors.toList());
    }

    public static List<JobAdminDTO> toAdminList(List<Job> jobs) {
        return jobs.stream().map(JobMapper::toAdmin).collect(Collectors.toList());
    }

    public static List<JobResponseDTO> toResponseList(List<Job> jobs) {
        return jobs.stream().map(JobMapper::toResponseDto).collect(Collectors.toList());
    }
}
