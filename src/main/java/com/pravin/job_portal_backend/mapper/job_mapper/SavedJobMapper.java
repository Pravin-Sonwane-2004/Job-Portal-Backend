package com.pravin.job_portal_backend.mapper.job_mapper;

import com.pravin.job_portal_backend.dto.job_dtos.SavedJobDTO;
import com.pravin.job_portal_backend.entity.SavedJob;

public class SavedJobMapper {

    // === Entity -> DTO ===
    public static SavedJobDTO toDTO(SavedJob entity) {
        if (entity == null)
            return null;

        return SavedJobDTO.builder()
                .id(entity.getId())
                .jobId(entity.getJob().getId())
                .jobTitle(entity.getJob().getTitle())
                .companyName(entity.getJob().getCompany() != null ? entity.getJob().getCompany().getName() : null)
                .location(entity.getJob().getLocation())
                .savedAt(entity.getSavedAt())
                .build();
    }
}
