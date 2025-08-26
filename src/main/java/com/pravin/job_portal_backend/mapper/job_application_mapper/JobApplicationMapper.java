package com.pravin.job_portal_backend.mapper.job_application_mapper;


import com.pravin.job_portal_backend.dto.job_dtos.JobApplicationAdminDTO;
import com.pravin.job_portal_backend.dto.job_dtos.JobApplicationResponseDTO;
import com.pravin.job_portal_backend.entity.JobApplication;

public class JobApplicationMapper {

    public static JobApplicationResponseDTO toResponse(JobApplication entity) {
        return JobApplicationResponseDTO.builder()
                .id(entity.getId())
                .jobId(entity.getJob().getId())
                .jobTitle(entity.getJob().getTitle())
                .userId(entity.getUser().getId())
                .userEmail(entity.getUser().getEmail())
                .status(entity.getStatus())
                .appliedAt(entity.getAppliedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static JobApplicationAdminDTO toAdmin(JobApplication entity) {
        return JobApplicationAdminDTO.builder()
                .applicationId(entity.getId())
                .jobTitle(entity.getJob().getTitle())
                // .companyName(entity.getJob().getCompany())
                .applicantName(entity.getUser().getName())
                .applicantEmail(entity.getUser().getEmail())
                .status(entity.getStatus())
                .build();
    }
}
