//package com.pravin.job_portal_backend.mapper.workremaining;
//
//import com.pravin.job_portal_backend.dto.ResumeDto;
//import com.pravin.job_portal_backend.entity.Resume;
//
//public class ResumeMapper {
//    public static ResumeDto toDto(Resume resume) {
//        if (resume == null) return null;
//        return ResumeDto.builder()
//                .id(resume.getId())
//                .userId(resume.getUser() != null ? resume.getUser().getId() : null)
//                .filePath(resume.getFilePath())
//                .uploadedAt(resume.getUploadedAt())
//                .build();
//    }
//
//    public static Resume toEntity(ResumeDto dto) {
//        if (dto == null) return null;
//        Resume resume = new Resume();
//        resume.setId(dto.getId());
//        // User should be set in service if needed
//        resume.setFilePath(dto.getFilePath());
//        resume.setUploadedAt(dto.getUploadedAt());
//        return resume;
//    }
//}
