package com.pravin.job_portal_backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.mapper.JobMapper;
import com.pravin.job_portal_backend.repository.JobsRepository;

@Service
public class JobServiceImpl implements com.pravin.job_portal_backend.service.interfaces.JobService {
    private final JobsRepository jobsRepository;

    @Autowired
    public JobServiceImpl(JobsRepository jobsRepository) {
        this.jobsRepository = jobsRepository;
    }

    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto) {
        Job job = JobMapper.toEntity(jobDto);
        Job saved = jobsRepository.save(job);
        return JobMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobDto updateJob(Long jobId, JobDto jobDto) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setTitle(jobDto.getTitle());
        job.setLocation(jobDto.getLocation());
        job.setSalary(jobDto.getSalary());
        job.setPostedDate(jobDto.getPostedDate());
        Job updated = jobsRepository.save(job);
        return JobMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId) {
        if (!jobsRepository.existsById(jobId)) {
            throw new RuntimeException("Job not found");
        }
        jobsRepository.deleteById(jobId);
    }

    @Override
    @Transactional(readOnly = true)
    public JobDto getJobById(Long jobId) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return JobMapper.toDto(job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobDto> getAllJobs() {
        return jobsRepository.findAll().stream()
                .map(JobMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobDto> searchJobs(String keyword, String location) {
        // Example: naive in-memory filtering. Replace with custom query for production.
        return jobsRepository.findAll().stream()
                .filter(job -> (keyword == null || job.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                        && (location == null || job.getLocation().toLowerCase().contains(location.toLowerCase())))
                .map(JobMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Job> getAllJobsOfPagable(int page, int size, String sortBy, String sortDir, String jobTitle,
        String jobLocation, Double minSalary, Double maxSalary) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
            page, size,
            "desc".equalsIgnoreCase(sortDir)
                ? org.springframework.data.domain.Sort.by(sortBy).descending()
                : org.springframework.data.domain.Sort.by(sortBy).ascending()
        );

        // Basic filtering logic (for demonstration; for production, use custom queries)
        Page<Job> jobsPage = jobsRepository.findAll(pageable);
        if (jobTitle != null || jobLocation != null || minSalary != null || maxSalary != null) {
            List<Job> filtered = jobsPage.getContent().stream()
                .filter(job -> (jobTitle == null || job.getTitle().toLowerCase().contains(jobTitle.toLowerCase())))
                .filter(job -> (jobLocation == null || job.getLocation().toLowerCase().contains(jobLocation.toLowerCase())))
                .filter(job -> {
                    if (minSalary == null) return true;
                    try {
                        return job.getSalary() != null && Double.parseDouble(job.getSalary().toString()) >= minSalary;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .filter(job -> {
                    if (maxSalary == null) return true;
                    try {
                        return job.getSalary() != null && Double.parseDouble(job.getSalary().toString()) <= maxSalary;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
            return new org.springframework.data.domain.PageImpl<>(filtered, pageable, filtered.size());
        }
        return jobsPage;
    }
}
