package com.pravin.job_portal_backend.controller.job;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import com.pravin.job_portal_backend.service.job_service.JobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // === Create Job (Admin only) ===
    @PostMapping("/add")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO jobDto) {
        JobResponseDTO created = jobService.createJob(jobDto);
        return ResponseEntity
                .created(URI.create("/api/jobs/" + created.getId()))
                .body(created);
    }

    // === Update Job (Admin only) ===
    @PutMapping("/{jobId}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobResponseDTO> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobRequestDTO jobDto) {
        return ResponseEntity.ok(jobService.updateJob(jobId, jobDto));
    }

    // === Hard Delete Job (Admin only) ===
    @DeleteMapping("/{jobId}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    // === Get Job by ID ===
    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    // // === List all jobs (lightweight for USERS) ===
    // @GetMapping
    // public ResponseEntity<List<JobSummaryDTO>> getAllJobs() {
    //     return ResponseEntity.ok(jobService.getAllJobs());
    // }

    // === Search jobs by keyword and location ===
    // @GetMapping("/search")
    // public ResponseEntity<List<JobSummaryDTO>> searchJobs(
    //         @RequestParam(required = false) String keyword,
    //         @RequestParam(required = false) String location) {
    //     return ResponseEntity.ok(jobService.searchJobs(keyword, location));
    // }

    // === Paginated & filtered jobs ===
    // @GetMapping("/paginated")
    // public ResponseEntity<Page<JobSummaryDTO>> getJobsPaginated(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size,
    //         @RequestParam(defaultValue = "postedDate") String sortBy,
    //         @RequestParam(defaultValue = "desc") String sortDir,
    //         @RequestParam(required = false) String jobTitle,
    //         @RequestParam(required = false) String jobLocation,
    //         @RequestParam(required = false) Double minSalary,
    //         @RequestParam(required = false) Double maxSalary) {

    //     return ResponseEntity.ok(
    //             jobService.getAllJobsPaginated(
    //                     page, size, sortBy, sortDir, jobTitle, jobLocation, minSalary, maxSalary));
    // }

    // === Get jobs by Company ID ===
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobSummaryDTO>> getJobsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId));
    }

    // === Admin: get all jobs including deleted ===
    @GetMapping("/admin")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobAdminDTO>> getAllJobsForAdmin() {
        return ResponseEntity.ok(jobService.getAllJobsForAdmin());
    }

    // === Close Job (Admin only) ===
    @PutMapping("/{jobId}/close")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> closeJob(@PathVariable Long jobId) {
        jobService.closeJob(jobId);
        return ResponseEntity.noContent().build();
    }

    // === Soft Delete Job (Admin only) ===
    @PutMapping("/{jobId}/delete")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> softDeleteJob(@PathVariable Long jobId) {
        jobService.markJobAsDeleted(jobId);
        return ResponseEntity.noContent().build();
    }

    // === Restore Job (Admin only) ===
    @PutMapping("/{jobId}/restore")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restoreJob(@PathVariable Long jobId) {
        jobService.restoreJob(jobId);
        return ResponseEntity.noContent().build();
    }
}
