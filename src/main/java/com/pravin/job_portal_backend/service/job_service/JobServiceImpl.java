package com.pravin.job_portal_backend.service.job_service;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.enums.JobStatus;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.mapper.job_mapper.JobMapper;
import com.pravin.job_portal_backend.repository.CompanyRepository;
import com.pravin.job_portal_backend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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

    @Override
    public JobResponseDTO createJob(JobRequestDTO dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Job job = JobMapper.toEntity(dto, company);
        job.setJobStatus(JobStatus.ACTIVE); // default status
        Job saved = jobRepository.save(job);
        return JobMapper.toResponse(saved);
    }

    @Override
    public JobResponseDTO updateJob(Long jobId, JobRequestDTO dto) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        JobMapper.updateEntity(job, dto, company);
        return JobMapper.toResponse(jobRepository.save(job));
    }

    @Override
    public void deleteJob(Long jobId) {
        jobRepository.deleteById(jobId); // Hard delete
    }

    @Override
    public JobResponseDTO getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return JobMapper.toResponse(job);
    }

    // @Override
    // public List<JobSummaryDTO> getAllJobs() {
    //     return jobRepository.findByStatus(JobStatus.ACTIVE).stream()
    //             .map(JobMapper::toSummary)
    //             .collect(Collectors.toList());
    // }

    // @Override
    // public List<JobSummaryDTO> getJobsByStatus(String status) {
    //     JobStatus jobStatus;
    //     try {
    //         jobStatus = JobStatus.valueOf(status.toUpperCase()); // convert string to enum
    //     } catch (IllegalArgumentException e) {
    //         throw new RuntimeException("Invalid job status: " + status);
    //     }

    //     return jobRepository.findByStatus(jobStatus).stream()
    //             .map(JobMapper::toSummary)
    //             .collect(Collectors.toList());
    // }

    // @Override
    // public List<JobSummaryDTO> searchJobs(String keyword, String location) {
    //     return jobRepository.searchJobs(keyword, location).stream()
    //             .map(JobMapper::toSummary)
    //             .collect(Collectors.toList());
    // }



    @Override
    public List<JobSummaryDTO> getJobsByCompany(Long companyId) {
        return jobRepository.findByCompanyId(companyId).stream()
                .map(JobMapper::toSummary)
                .collect(Collectors.toList());
    }

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

    // === Soft delete ===
    @Override
    public void markJobAsDeleted(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setJobStatus(JobStatus.DELETED);
        jobRepository.save(job);
    }

    // === Restore soft-deleted job ===
    @Override
    public void restoreJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setJobStatus(JobStatus.ACTIVE);
        jobRepository.save(job);
    }

    // === Close job ===
    @Override
    public void closeJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setJobStatus(JobStatus.CLOSED);
        jobRepository.save(job);

    }


    // @Override
    // public Page<JobSummaryDTO> getAllJobsPaginated(
    //         int page, int size, String sortBy, String sortDir,
    //         String jobTitle, String jobLocation,
    //         Double minSalary, Double maxSalary) {

    //     Pageable pageable = PageRequest.of(
    //             page,
    //             size,
    //             sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

    //     // Pass nulls if not provided
    //     Page<Job> jobs = jobRepository.filterJobs(
    //             (jobTitle == null || jobTitle.isBlank()) ? null : jobTitle,
    //             (jobLocation == null || jobLocation.isBlank()) ? null : jobLocation,
    //             minSalary,
    //             maxSalary,
    //             pageable);

    //     return jobs.map(JobMapper::toSummary);
    // }

}
