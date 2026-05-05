package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.JobDto;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Defines the business operations for jobs.
 *
 * Controllers depend on this interface, while JobServiceImpl contains the real
 * logic. This makes the controller easy to explain: it calls a use case method
 * instead of knowing how jobs are stored or filtered.
 */
public interface JobService {
    /** Admin/system use case: create a job without tying it to a recruiter. */
    JobDto createJob(JobDto jobDto);

    /** Recruiter use case: create a job and store who posted it. */
    JobDto createJobForRecruiter(JobDto jobDto, String recruiterEmail);

    /** Admin/system update where ownership is not checked. */
    JobDto updateJob(Long jobId, JobDto jobDto);

    /** Recruiter update where the service checks the job belongs to them. */
    JobDto updateRecruiterJob(Long jobId, JobDto jobDto, String recruiterEmail);

    void deleteJob(Long jobId);

    /** Deletes only if the logged-in recruiter owns the job. */
    void deleteRecruiterJob(Long jobId, String recruiterEmail);

    JobDto getJobById(Long jobId);

    /** Fetches one recruiter-owned job and blocks access to other recruiters' jobs. */
    JobDto getRecruiterJobById(Long jobId, String recruiterEmail);

    List<JobDto> getAllJobs();

    List<JobDto> getJobsByRecruiter(String recruiterEmail);

    /** Basic keyword/location search for public job discovery. */
    List<JobDto> searchJobs(String keyword, String location);

    /** Returns jobs with pagination, sorting, and optional filters for listing pages. */
    Page<JobDto> getAllJobsOfPagable(int page, int size, String sortBy, String sortDir, String jobTitle,
        String jobLocation, Double minSalary, Double maxSalary);
}
