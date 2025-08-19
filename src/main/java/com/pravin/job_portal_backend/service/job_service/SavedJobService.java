package com.pravin.job_portal_backend.service.job_service;

import java.util.List;

import com.pravin.job_portal_backend.dto.job_dtos.SavedJobDto;

public interface SavedJobService {
   SavedJobDto saveJob(Long JOB_SEEKERId, Long jobId);
   void unsaveJob(Long JOB_SEEKERId, Long jobId);
   List<SavedJobDto> getSavedJobsByJOB_SEEKER(Long JOB_SEEKERId);
}
