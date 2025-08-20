package com.pravin.job_portal_backend.service.job_service;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.enums.JobStatus;
import com.pravin.job_portal_backend.mapper.job_mapper.JobMapper;
import com.pravin.job_portal_backend.repository.CompanyRepository;
import com.pravin.job_portal_backend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    // === Create Job ===
    @Override
    public JobResponseDTO createJob(JobRequestDTO dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Job job = JobMapper.toEntity(dto, company);
        job.setJobStatus(JobStatus.ACTIVE); // default status
        Job saved = jobRepository.save(job);
        return JobMapper.toResponse(saved);
    }

    // === Update Job ===
    @Override
    public JobResponseDTO updateJob(Long jobId, JobRequestDTO dto) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        JobMapper.updateEntity(job, dto, company);
        return JobMapper.toResponse(jobRepository.save(job));
    }

    // === Hard Delete ===
    @Override
    public void deleteJob(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException("Job not found");
        }
        jobRepository.deleteById(jobId);
    }

    // === Get Job by Id ===
    @Override
    public JobResponseDTO getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return JobMapper.toResponse(job);
    }

    // === Get All Jobs (User Facing) ===
    @Override
    public List<JobResponseDTO> getAllJobs() {
        return jobRepository.findAll().stream()
                .filter(job -> job.getJobStatus() == JobStatus.ACTIVE) // ✅ show only active jobs
                .map(JobMapper::toResponse)
                .collect(Collectors.toList());
    }

    // === Get Jobs by Company ===
    @Override
    public List<JobSummaryDTO> getJobsByCompany(Long companyId) {
        return jobRepository.findByCompanyId(companyId).stream()
                .map(JobMapper::toSummary)
                .collect(Collectors.toList());
    }

    // === Admin: Get All Jobs ===
    @Override
    public List<JobAdminDTO> getAllJobsForAdmin() {
        return jobRepository.findAll().stream()
                .map(job -> new JobAdminDTO(
                        job.getId(),
                        job.getTitle(),
                        job.getLocation(),
                        job.getJobStatus(),
                        job.getCompany() != null ? job.getCompany().getName() : null))
                .collect(Collectors.toList());
    }

    // === Soft Delete ===
    @Override
    public void markJobAsDeleted(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setJobStatus(JobStatus.DELETED);
        jobRepository.save(job);
    }

    // === Restore Job ===
    @Override
    public void restoreJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setJobStatus(JobStatus.ACTIVE);
        jobRepository.save(job);
    }

    // === Close Job ===
    @Override
    public void closeJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setJobStatus(JobStatus.CLOSED);
        jobRepository.save(job);
    }
}
