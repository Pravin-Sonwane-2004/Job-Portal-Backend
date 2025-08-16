//package com.pravin.job_portal_backend.service.interfaces;
//
//import com.pravin.job_portal_backend.dto.JobDto;
//import com.pravin.job_portal_backend.entity.Job;
//
//import java.util.List;
//
//import org.springframework.data.domain.Page;
//
//public interface JobService {
//    JobDto createJob(JobDto jobDto);
//    JobDto updateJob(Long jobId, JobDto jobDto);
//    void deleteJob(Long jobId);
//    JobDto getJobById(Long jobId);
//    List<JobDto> getAllJobs();
//    List<JobDto> searchJobs(String keyword, String location);
//    Page<Job> getAllJobsOfPagable(int page, int size, String sortBy, String sortDir, String jobTitle,
//        String jobLocation, Double minSalary, Double maxSalary);
//}
