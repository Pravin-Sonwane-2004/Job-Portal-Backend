package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.ResumeDto;
import com.pravin.job_portal_backend.entity.User;

import java.util.List;

public interface ResumeService {
    ResumeDto uploadResume(Long userId, ResumeDto resumeDto);
    ResumeDto updateResume(Long resumeId, ResumeDto resumeDto);
    void deleteResume(Long resumeId);
    ResumeDto getResumeById(Long resumeId);
    List<ResumeDto> getResumesByUser(Long userId);
    // Optionally, keep the old signatures if needed for other use cases
    ResumeDto uploadResume(User user, String filePath);
    List<ResumeDto> getResumesByUser(User user);
}
