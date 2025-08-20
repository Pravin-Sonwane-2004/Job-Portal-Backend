package com.pravin.job_portal_backend.controller.job;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import com.pravin.job_portal_backend.service.job_service.JobService;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // === Create Job ===
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO jobDto) {
        JobResponseDTO created = jobService.createJob(jobDto);
        return ResponseEntity
                .created(URI.create("/api/jobs/" + created.getId()))
                .body(created);
    }

    // === Update Job ===
    @PutMapping("/{jobId}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobResponseDTO> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobRequestDTO jobDto) {
        return ResponseEntity.ok(jobService.updateJob(jobId, jobDto));
    }

    // === Hard Delete Job ===
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

    // === Get All Jobs ===
    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // === Get Jobs by Company ===
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobSummaryDTO>> getJobsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId));
    }

    // === Admin-only Endpoints ===
    @GetMapping("/admin")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobAdminDTO>> getAllJobsForAdmin() {
        return ResponseEntity.ok(jobService.getAllJobsForAdmin());
    }

    @PatchMapping("/{jobId}/close")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> closeJob(@PathVariable Long jobId) {
        jobService.closeJob(jobId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{jobId}/soft-delete")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> softDeleteJob(@PathVariable Long jobId) {
        jobService.markJobAsDeleted(jobId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{jobId}/restore")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restoreJob(@PathVariable Long jobId) {
        jobService.restoreJob(jobId);
        return ResponseEntity.noContent().build();
    }
}
