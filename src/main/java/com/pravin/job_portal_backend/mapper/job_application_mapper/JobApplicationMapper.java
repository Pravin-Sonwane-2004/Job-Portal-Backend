package com.pravin.job_portal_backend.mapper.job_application_mapper;

import com.pravin.job_portal_backend.dto.job_application_dtos.ApplyJobRequestDTO;
import com.pravin.job_portal_backend.dto.job_application_dtos.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.dto.job_application_dtos.ApplyJobSummaryDTO;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.JobApplication;
import com.pravin.job_portal_backend.entity.User;

public class JobApplicationMapper {

    // === RequestDTO -> Entity ===
    public static JobApplication toEntity(ApplyJobRequestDTO dto, Job job, User user) {
        if (dto == null || job == null || user == null)
            return null;

        return JobApplication.builder()
                .job(job)
                .user(user)
                .build();
    }

    // === Entity -> ResponseDTO ===
    public static ApplyJobResponseDTO toResponseDTO(JobApplication entity) {
        if (entity == null)
            return null;

        return ApplyJobResponseDTO.builder()
                .applicationId(entity.getId())
                .jobId(entity.getJob().getId())
                .status(entity.getStatus())
                .appliedAt(entity.getAppliedAt())
                .build();
    }

    // === Entity -> SummaryDTO ===
    public static ApplyJobSummaryDTO toSummaryDTO(JobApplication entity) {
        if (entity == null)
            return null;

        return ApplyJobSummaryDTO.builder()
                .jobId(entity.getJob().getId())
                .jobTitle(entity.getJob().getTitle())
                .companyName(entity.getJob().getCompany() != null ? entity.getJob().getCompany().getName() : null)
                .location(entity.getJob().getLocation())
                .status(entity.getStatus())
                .build();
    }
}
