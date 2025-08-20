package com.pravin.job_portal_backend.mapper.apply_job_mapper;


import org.springframework.stereotype.Component;

import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.dto.job_application_dtos.ApplyJobRequestDTO;
import com.pravin.job_portal_backend.dto.job_application_dtos.ApplyJobSummaryDTO;
import com.pravin.job_portal_backend.entity.ApplyJob;

@Component
public class ApplyJobMapper {

    public ApplyJob toEntity(ApplyJobRequestDTO dto) {
        if (dto == null)
            return null;

        return ApplyJob.builder()
                .coverLetter(dto.getCoverLetter())
                .build();
    }

    public ApplyJobResponseDTO toResponseDTO(ApplyJob entity) {
        if (entity == null)
            return null;

        return ApplyJobResponseDTO.builder()
                .id(entity.getId())
                .job(entity.getJob()        )
                .userId(entity.getUser().getId())
                .coverLetter(entity.getCoverLetter())
                .status(entity.getStatus())
                .appliedAt(entity.getAppliedAt())
                .build();
    }

    public ApplyJobSummaryDTO toSummaryDTO(ApplyJob entity) {
        if (entity == null)
            return null;

        return ApplyJobSummaryDTO.builder()
                .id(entity.getId())
                .jobId(entity.getJob().getId())
                .jobTitle(entity.getJob().getTitle())
                .companyName(entity.getJob().getCompany())
                .status(entity.getStatus())
                .build();
    }
}
