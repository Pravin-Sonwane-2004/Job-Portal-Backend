package com.pravin.job_portal_backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.job_portal_backend.dto.SavedJobDto;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.SavedJob;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.mapper.SavedJobMapper;
import com.pravin.job_portal_backend.repository.JobsRepository;
import com.pravin.job_portal_backend.repository.SavedJobRepository;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.SavedJobService;

@Service
public class SavedJobServiceImpl implements SavedJobService {
  @Autowired
  private SavedJobRepository savedJobRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JobsRepository jobsRepository;

  @Override
  @Transactional
  public SavedJobDto saveJob(Long userId, Long jobId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    Job job = jobsRepository.findById(jobId)
        .orElseThrow(() -> new RuntimeException("Job not found"));

    return savedJobRepository.findByUserAndJob(user, job)
        .map(SavedJobMapper::toDto)
        .orElseGet(() -> SavedJobMapper.toDto(savedJobRepository.save(
            SavedJob.builder()
                .user(user)
                .job(job)
                .build())));
  }

  @Override
  @Transactional
  public void unsaveJob(Long userId, Long jobId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    Job job = jobsRepository.findById(jobId)
        .orElseThrow(() -> new RuntimeException("Job not found"));
    savedJobRepository.deleteByUserAndJob(user, job);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SavedJobDto> getSavedJobsByUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return savedJobRepository.findByUser(user).stream()
        .map(SavedJobMapper::toDto)
        .collect(Collectors.toList());
  }

}
