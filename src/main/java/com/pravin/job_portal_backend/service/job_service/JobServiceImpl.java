package com.pravin.job_portal_backend.service.job_service;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.exception.ResourceNotFoundException;
import com.pravin.job_portal_backend.mapper.job_mapper.JobMapper;
import com.pravin.job_portal_backend.repository.JobsRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobsRepository jobsRepository;

    public JobServiceImpl(JobsRepository jobsRepository) {
        this.jobsRepository = jobsRepository;
    }

    @Transactional
    public JobResponseDTO createJob(JobRequestDTO jobDto) {
        Job job = JobMapper.toEntity(jobDto);
        Job saved = jobsRepository.save(job);
        return JobMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public JobResponseDTO updateJob(Long jobId, JobRequestDTO jobDto) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        JobMapper.updateEntityFromDto(jobDto, job);
        Job updated = jobsRepository.save(job);
        return JobMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        jobsRepository.delete(job);
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponseDTO getJobById(Long jobId) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        return JobMapper.toResponseDto(job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobSummaryDTO> getAllJobs() {
        return jobsRepository.findAll().stream()
                .filter(job -> !job.isDeleted())
                .map(JobMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobSummaryDTO> searchJobs(String keyword, String location) {
        return jobsRepository.findAll().stream()
                .filter(job -> !job.isDeleted() &&
                        (keyword == null || job.getTitle().toLowerCase().contains(keyword.toLowerCase())) &&
                        (location == null || job.getLocation().toLowerCase().contains(location.toLowerCase())))
                .map(JobMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobSummaryDTO> getAllJobsPaginated(
            int page, int size, String sortBy, String sortDir,
            String jobTitle, String jobLocation,
            Double minSalary, Double maxSalary) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Job> jobsPage = jobsRepository.findAll(pageable);

        List<JobSummaryDTO> filtered = jobsPage.getContent().stream()
                .filter(job -> !job.isDeleted())
                .filter(job -> jobTitle == null || job.getTitle().toLowerCase().contains(jobTitle.toLowerCase()))
                .filter(job -> jobLocation == null
                        || job.getLocation().toLowerCase().contains(jobLocation.toLowerCase()))
                .filter(job -> minSalary == null || (job.getMinSalary() != null && job.getMinSalary() >= minSalary))
                .filter(job -> maxSalary == null || (job.getMaxSalary() != null && job.getMaxSalary() <= maxSalary))
                .map(JobMapper::toSummaryDto)
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, pageable, jobsPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobAdminDTO> getAllJobsForAdmin() {
        return jobsRepository.findAll().stream()
                .map(JobMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void closeJob(Long jobId) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        job.setStatus("CLOSED");
        jobsRepository.save(job);
    }

    @Override
    @Transactional
    public void markJobAsDeleted(Long jobId) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        job.setDeleted(true);
        jobsRepository.save(job);
    }

    @Override
    @Transactional
    public void restoreJob(Long jobId) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        job.setDeleted(false);
        jobsRepository.save(job);
    }

}
