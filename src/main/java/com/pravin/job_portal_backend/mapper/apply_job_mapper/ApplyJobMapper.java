package com.pravin.job_portal_backend.mapper.apply_job_mapper;

import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobDto;
import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.entity.ApplyJob;

public class ApplyJobMapper {

    public static ApplyJobDto toDto(ApplyJob entity) {
        if (entity == null)
            return null;

        return ApplyJobDto.builder()
                .applicationId(entity.getId())
                .userId(entity.getUser().getId())
                .jobId(entity.getJob().getId())
                .status(entity.getStatus())
                .resumeLink(entity.getResumeLink())
                .coverLetter(entity.getCoverLetter())
                .appliedFromIp(entity.getAppliedFromIp())
                .source(entity.getSource())
                .userAgent(entity.getUserAgent())
                .appliedAt(entity.getAppliedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static ApplyJobResponseDTO toResponseDto(ApplyJob entity) {
        if (entity == null)
            return null;

        return ApplyJobResponseDTO.builder()
                .applicationId(entity.getId())
                .jobTitle(entity.getJob().getTitle())
                .company(entity.getJob().getCompany())
                .userEmail(entity.getUser().getEmail())
                .appliedAt(entity.getAppliedAt())
                .status(entity.getStatus())
                .resumeLink(entity.getResumeLink())
                .coverLetter(entity.getCoverLetter())
                .jobLocation(entity.getJob().getLocation())
                .jobSalary(entity.getJob().getMaxSalary())
                .build();
    }
}
