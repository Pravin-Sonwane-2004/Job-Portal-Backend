package com.pravin.job_portal_backend.controller.admin;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import com.pravin.job_portal_backend.service.job_service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/jobs")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminJobController {

    private final JobService jobService;

    // === Create Job ===
    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody JobRequestDTO jobDto) {
        JobResponseDTO created = jobService.createJob(jobDto);
        return ResponseEntity.status(201).body(created);
    }

    // === Update Job ===
    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponseDTO> updateJob(@PathVariable Long jobId,
            @RequestBody JobRequestDTO jobDto) {
        JobResponseDTO updated = jobService.updateJob(jobId, jobDto);
        return ResponseEntity.ok(updated);
    }

    // === Hard Delete Job ===
    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    // === Get Job by ID ===
    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long jobId) {
        JobResponseDTO job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    // === List all jobs for admin ===
    @GetMapping
    public ResponseEntity<List<JobAdminDTO>> getAllJobs() {
        List<JobAdminDTO> jobs = jobService.getAllJobsForAdmin();
        return ResponseEntity.ok(jobs);
    }

    // === Paginated & filtered jobs ===
    @GetMapping("/paginated")
    public ResponseEntity<Page<JobSummaryDTO>> getJobsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) String jobLocation,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary) {
        Page<JobSummaryDTO> jobs = jobService.getAllJobsPaginated(
                page, size, sortBy, sortDir, jobTitle, jobLocation, minSalary, maxSalary);
        return ResponseEntity.ok(jobs);
    }

    // === Close Job ===
    @PutMapping("/{jobId}/close")
    public ResponseEntity<Void> closeJob(@PathVariable Long jobId) {
        jobService.closeJob(jobId);
        return ResponseEntity.noContent().build();
    }

    // === Soft Delete Job ===
    @PutMapping("/{jobId}/delete")
    public ResponseEntity<Void> softDeleteJob(@PathVariable Long jobId) {
        jobService.markJobAsDeleted(jobId);
        return ResponseEntity.noContent().build();
    }

    // === Restore Job ===
    @PutMapping("/{jobId}/restore")
    public ResponseEntity<Void> restoreJob(@PathVariable Long jobId) {
        jobService.restoreJob(jobId);
        return ResponseEntity.noContent().build();
    }

    // === Search Jobs ===
    @GetMapping("/search")
    public ResponseEntity<List<JobSummaryDTO>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location) {
        List<JobSummaryDTO> jobs = jobService.searchJobs(keyword, location);
        return ResponseEntity.ok(jobs);
    }
}
