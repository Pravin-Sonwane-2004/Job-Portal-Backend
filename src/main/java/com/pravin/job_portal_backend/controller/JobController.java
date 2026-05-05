package com.pravin.job_portal_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.service.interfaces.JobService;

import java.util.List;

/**
 * Public/user-facing job API.
 *
 * Flow:
 * HTTP request -> controller reads request parameters -> JobService applies job
 * listing/search rules -> controller returns JobDto responses.
 */
@RestController
@RequestMapping("/user")
public class JobController {

  private final JobService jobService;

  public JobController(JobService jobService) {
    this.jobService = jobService;
  }

  /**
   * Simple listing endpoint used when the frontend needs all jobs.
   */
  @GetMapping("/jobs")
  public ResponseEntity<List<JobDto>> getAllJobs() {
    return ResponseEntity.ok(jobService.getAllJobs());
  }

  /**
   * Fetches one job by id. The service throws an error if the job is missing.
   */
  @GetMapping("/jobs/{id}")
  public ResponseEntity<JobDto> getJobById(@PathVariable Long id) {
    return ResponseEntity.ok(jobService.getJobById(id));
  }

  /**
   * Listing endpoint for pages that need pagination, sorting, and filters.
   */
  @GetMapping("/jobs/sorted")
  public ResponseEntity<Page<JobDto>> getAllJobsSortedPaginated(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "postedDate") String sortBy,
      @RequestParam(defaultValue = "asc") String direction,
      @RequestParam(required = false) String jobTitle,
      @RequestParam(required = false) String jobLocation,
      @RequestParam(required = false) Double minSalary,
      @RequestParam(required = false) Double maxSalary) {
    return ResponseEntity.ok(
        jobService.getAllJobsOfPagable(page, size, sortBy, direction, jobTitle, jobLocation, minSalary, maxSalary));
  }
}
