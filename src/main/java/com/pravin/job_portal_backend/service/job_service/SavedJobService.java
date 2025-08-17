package com.pravin.job_portal_backend.service.job_service;

import java.util.List;

import com.pravin.job_portal_backend.dto.job_dtos.SavedJobDto;

public interface SavedJobService {
   SavedJobDto saveJob(Long userId, Long jobId);
   void unsaveJob(Long userId, Long jobId);
   List<SavedJobDto> getSavedJobsByUser(Long userId);
}
