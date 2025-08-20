package com.pravin.job_portal_backend.service.job_service;

import java.util.List;

import com.pravin.job_portal_backend.entity.SavedJob;

public interface SavedJobService {
   SavedJob saveJob(Long JOB_SEEKERId, Long jobId);
   void unsaveJob(Long JOB_SEEKERId, Long jobId);
   List<SavedJob> getSavedJobsByJOB_SEEKER(Long JOB_SEEKERId);
}
