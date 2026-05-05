package com.pravin.job_portal_backend.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.mapper.JobMapper;
import com.pravin.job_portal_backend.repository.JobsRepository;
import com.pravin.job_portal_backend.repository.UserRepository;

@Service
public class JobServiceImpl implements com.pravin.job_portal_backend.service.interfaces.JobService {
    private final JobsRepository jobsRepository;
    private final UserRepository userRepository;

    public JobServiceImpl(JobsRepository jobsRepository, UserRepository userRepository) {
        this.jobsRepository = jobsRepository;
        this.userRepository = userRepository;
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
    public JobDto createJobForRecruiter(JobDto jobDto, String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        Job job = JobMapper.toEntity(jobDto);
        job.setPostedBy(recruiter);
        Job saved = jobsRepository.save(job);
        return JobMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobDto updateJob(Long jobId, JobDto jobDto) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        applyJobUpdates(job, jobDto);
        Job updated = jobsRepository.save(job);
        return JobMapper.toDto(updated);
    }

    @Override
    @Transactional
    public JobDto updateRecruiterJob(Long jobId, JobDto jobDto, String recruiterEmail) {
        Job job = getOwnedJob(jobId, recruiterEmail);
        applyJobUpdates(job, jobDto);
        return JobMapper.toDto(jobsRepository.save(job));
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
    @Transactional
    public void deleteRecruiterJob(Long jobId, String recruiterEmail) {
        Job job = getOwnedJob(jobId, recruiterEmail);
        jobsRepository.delete(job);
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
    public JobDto getRecruiterJobById(Long jobId, String recruiterEmail) {
        return JobMapper.toDto(getOwnedJob(jobId, recruiterEmail));
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
    public List<JobDto> getJobsByRecruiter(String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return jobsRepository.findByPostedBy(recruiter).stream()
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
    @Transactional(readOnly = true)
    public Page<JobDto> getAllJobsOfPagable(int page, int size, String sortBy, String sortDir, String jobTitle,
        String jobLocation, Double minSalary, Double maxSalary) {
        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(resolveSortBy(sortBy)).descending()
                : Sort.by(resolveSortBy(sortBy)).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        // Basic filtering logic (for demonstration; for production, use custom queries)
        Page<Job> jobsPage = jobsRepository.findAll(pageable);
        if (jobTitle != null || jobLocation != null || minSalary != null || maxSalary != null) {
            List<Job> filtered = jobsPage.getContent().stream()
                .filter(job -> (jobTitle == null || contains(job.getTitle(), jobTitle)))
                .filter(job -> (jobLocation == null || contains(job.getLocation(), jobLocation)))
                .filter(job -> {
                    if (minSalary == null) return true;
                    try {
                        return job.getSalary() != null && Double.parseDouble(job.getSalary()) >= minSalary;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .filter(job -> {
                    if (maxSalary == null) return true;
                    try {
                        return job.getSalary() != null && Double.parseDouble(job.getSalary()) <= maxSalary;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
            List<JobDto> dtoContent = filtered.stream().map(JobMapper::toDto).collect(Collectors.toList());
            return new PageImpl<>(dtoContent, pageable, filtered.size());
        }
        return jobsPage.map(JobMapper::toDto);
    }

    private String resolveSortBy(String sortBy) {
        Map<String, String> aliases = Map.of(
                "jobTitle", "title",
                "jobLocation", "location",
                "jobSalary", "salary",
                "title", "title",
                "location", "location",
                "salary", "salary",
                "postedDate", "postedDate",
                "id", "id");
        return aliases.getOrDefault(sortBy, "postedDate");
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase().contains(query.toLowerCase());
    }

    private Job getOwnedJob(Long jobId, String recruiterEmail) {
        Job job = jobsRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        if (job.getPostedBy() == null || !recruiterEmail.equalsIgnoreCase(job.getPostedBy().getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException("You can only manage your own jobs.");
        }
        return job;
    }

    private void applyJobUpdates(Job job, JobDto jobDto) {
        if (jobDto.getTitle() != null) job.setTitle(jobDto.getTitle());
        if (jobDto.getLocation() != null) job.setLocation(jobDto.getLocation());
        if (jobDto.getSalary() != null) job.setSalary(jobDto.getSalary());
        if (jobDto.getCompany() != null) job.setCompany(jobDto.getCompany());
        if (jobDto.getDescription() != null) job.setDescription(jobDto.getDescription());
        if (jobDto.getRequirements() != null) job.setRequirements(jobDto.getRequirements());
        if (jobDto.getJobType() != null) job.setJobType(jobDto.getJobType());
        if (jobDto.getExperienceLevel() != null) job.setExperienceLevel(jobDto.getExperienceLevel());
        if (jobDto.getStatus() != null) job.setStatus(jobDto.getStatus());
        if (jobDto.getCategory() != null) job.setCategory(jobDto.getCategory());
        if (jobDto.getLastDateToApply() != null) job.setLastDateToApply(jobDto.getLastDateToApply());
    }
}
