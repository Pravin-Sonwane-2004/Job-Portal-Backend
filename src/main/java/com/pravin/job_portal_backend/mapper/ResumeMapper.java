package com.pravin.job_portal_backend.mapper;

import com.pravin.job_portal_backend.dto.ResumeDto;
import com.pravin.job_portal_backend.entity.Resume;

public class ResumeMapper {
    public static ResumeDto toDto(Resume resume) {
        if (resume == null) return null;

        ResumeDto dto = new ResumeDto();
        dto.setId(resume.getId());
        dto.setUserId(resume.getUser() != null ? resume.getUser().getId() : null);
        dto.setFilePath(resume.getFilePath());
        dto.setUploadedAt(resume.getUploadedAt());
        return dto;
    }

    public static Resume toEntity(ResumeDto dto) {
        if (dto == null) return null;
        Resume resume = new Resume();
        resume.setId(dto.getId());
        // User should be set in service if needed
        resume.setFilePath(dto.getFilePath());
        resume.setUploadedAt(dto.getUploadedAt());
        return resume;
    }
}
