package com.pravin.job_portal_backend.service.impl;

import com.pravin.job_portal_backend.dto.ResumeDto;
import org.springframework.stereotype.Service;
import java.util.List;

import com.pravin.job_portal_backend.entity.Resume;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.mapper.ResumeMapper;
import com.pravin.job_portal_backend.repository.ResumeRepository;
import com.pravin.job_portal_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

/**
 * Handles resume-related business logic.
 *
 * Flow:
 * Controller passes a user/resume request -> service validates related data ->
 * mapper converts DTO/entity -> repository saves or reads from the database.
 */
@Service
public class ResumeServiceImpl implements com.pravin.job_portal_backend.service.interfaces.ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public ResumeDto uploadResume(Long userId, ResumeDto resumeDto) {
        // First load the owner so every resume is linked to a real user.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // DTO carries API data; mapper creates the database entity.
        Resume resume = ResumeMapper.toEntity(resumeDto);
        resume.setUser(user);
        Resume saved = resumeRepository.save(resume);
        return ResumeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ResumeDto updateResume(Long resumeId, ResumeDto resumeDto) {
        // Load the existing resume, update only editable fields, then save it.
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        resume.setFilePath(resumeDto.getFilePath());
        resume.setUploadedAt(resumeDto.getUploadedAt());
        Resume updated = resumeRepository.save(resume);
        return ResumeMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteResume(Long resumeId) {
        // Existence check lets us return a clear error instead of silently doing nothing.
        if (!resumeRepository.existsById(resumeId)) {
            throw new RuntimeException("Resume not found");
        }
        resumeRepository.deleteById(resumeId);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeDto getResumeById(Long resumeId) {
        // Read-only transaction is used because this method only fetches data.
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        return ResumeMapper.toDto(resume);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeDto> getResumesByUser(Long userId) {
        // The service accepts userId from the controller and converts it to a User entity.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return resumeRepository.findByUser(user).stream()
                .map(ResumeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResumeDto uploadResume(User user, String filePath) {
        // Convenience method for callers that already loaded the User entity.
        Resume resume = new Resume();
        resume.setUser(user);
        resume.setFilePath(filePath);
        Resume saved = resumeRepository.save(resume);
        return ResumeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeDto> getResumesByUser(User user) {
        // Convenience read method for callers that already have the User entity.
        return resumeRepository.findByUser(user).stream()
                .map(ResumeMapper::toDto)
                .collect(Collectors.toList());
    }
}
