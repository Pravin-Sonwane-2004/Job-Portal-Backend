//package com.pravin.job_portal_backend.mapper.workremaining;
//
//import com.pravin.job_portal_backend.dto.SavedJobDto;
//import com.pravin.job_portal_backend.entity.SavedJob;
//
//public class SavedJobMapper {
//    public static SavedJobDto toDto(SavedJob savedJob) {
//        if (savedJob == null) return null;
//        return SavedJobDto.builder()
//                .id(savedJob.getId())
//                .userId(savedJob.getUser() != null ? savedJob.getUser().getId() : null)
//                .jobId(savedJob.getJob() != null ? savedJob.getJob().getId() : null)
//                .savedAt(savedJob.getSavedAt())
//                .build();
//    }
//
//    public static SavedJob toEntity(SavedJobDto dto) {
//        if (dto == null) return null;
//        SavedJob savedJob = new SavedJob();
//        savedJob.setId(dto.getId());
//        // User and Job should be set in service if needed
//        savedJob.setSavedAt(dto.getSavedAt());
//        return savedJob;
//    }
//}
