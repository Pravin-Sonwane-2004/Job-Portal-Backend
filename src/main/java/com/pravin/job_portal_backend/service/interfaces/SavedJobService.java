package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.SavedJobDto;
import com.pravin.job_portal_backend.entity.User;

import java.util.List;

public interface SavedJobService {
    SavedJobDto saveJob(Long userId, Long jobId);
    void unsaveJob(Long userId, Long jobId);
    List<SavedJobDto> getSavedJobsByUser(Long userId);
}
