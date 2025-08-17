package com.pravin.job_portal_backend.controller.job;

import com.pravin.job_portal_backend.dto.job_dtos.JobResponseDTO;
import com.pravin.job_portal_backend.dto.job_dtos.JobSummaryDTO;
import com.pravin.job_portal_backend.service.job_service.JobService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    private final JobService jobService;

    public PublicController(JobService jobService) {
        this.jobService = jobService;
    }

    // === Health check ===
    @GetMapping("/health-check")
    public String healthCheck() {
        log.info("Health check endpoint called");
        return "Ok";
    }

    // === List all jobs (lightweight summaries) ===
    @GetMapping("/jobs")
    public ResponseEntity<List<JobSummaryDTO>> getAllJobs() {
        List<JobSummaryDTO> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    // === Get job by ID ===
    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long jobId) {
        JobResponseDTO job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    // === Paginated & filtered jobs ===
    @GetMapping("/jobs/paginated")
    public ResponseEntity<Map<String, Object>> getAllJobsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postedDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) String jobLocation,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary) {

        Page<JobSummaryDTO> pageResult = jobService.getAllJobsPaginated(
                page, size, sortBy, sortDir, jobTitle, jobLocation, minSalary, maxSalary);

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageResult.getContent());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("totalElements", pageResult.getTotalElements());
        response.put("pageNumber", pageResult.getNumber());
        response.put("pageSize", pageResult.getSize());

        return ResponseEntity.ok(response);
    }

    // === Search jobs ===
    @GetMapping("/jobs/search")
    public ResponseEntity<List<JobSummaryDTO>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location) {

        List<JobSummaryDTO> jobs = jobService.searchJobs(keyword, location);
        return ResponseEntity.ok(jobs);
    }
}
