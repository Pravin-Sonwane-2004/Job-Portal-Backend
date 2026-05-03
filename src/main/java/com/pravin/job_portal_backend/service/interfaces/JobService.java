package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.entity.Job;

import java.util.List;

import org.springframework.data.domain.Page;

public interface JobService {
    JobDto createJob(JobDto jobDto);
    JobDto createJobForRecruiter(JobDto jobDto, String recruiterEmail);
    JobDto updateJob(Long jobId, JobDto jobDto);
    JobDto updateRecruiterJob(Long jobId, JobDto jobDto, String recruiterEmail);
    void deleteJob(Long jobId);
    void deleteRecruiterJob(Long jobId, String recruiterEmail);
    JobDto getJobById(Long jobId);
    JobDto getRecruiterJobById(Long jobId, String recruiterEmail);
    List<JobDto> getAllJobs();
    List<JobDto> getJobsByRecruiter(String recruiterEmail);
    List<JobDto> searchJobs(String keyword, String location);
    Page<Job> getAllJobsOfPagable(int page, int size, String sortBy, String sortDir, String jobTitle,
        String jobLocation, Double minSalary, Double maxSalary);
}
