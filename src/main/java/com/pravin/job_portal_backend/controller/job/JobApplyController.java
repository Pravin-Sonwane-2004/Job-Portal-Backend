package com.pravin.job_portal_backend.controller.job;

import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobDto;
import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.dto.job_dtos.ApplicationProfileDtoAdmin;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.job_service.JobApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/apply/applications")
public class JobApplyController {

    @Autowired
    private JobApplyService jobApplicationService;

    @Autowired
    private UserRepository userRepository;

    // 1. Apply to a job
    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> applyToJob(Authentication authentication, @RequestParam Long jobId) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String response = jobApplicationService.applyForJob(user.getId(), jobId);
        return ResponseEntity.ok(response);
    }

    // 2. Delete application by application ID
    @DeleteMapping("/{applicationId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteApplicationById(@PathVariable Long applicationId) {
        jobApplicationService.deleteApplicationById(applicationId);
        return ResponseEntity.ok("Application deleted successfully.");
    }

    // 3. Update application by application ID
    @PutMapping("/{applicationId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> updateApplicationById(@PathVariable Long applicationId,
            @RequestBody Map<String, Object> updates) {
        jobApplicationService.updateApplicationById(applicationId, updates);
        return ResponseEntity.ok("Application updated successfully.");
    }

    // 4. Get applied jobs as DTO
    @GetMapping("/my-applied-dto")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ApplyJobResponseDTO>> getAppliedJobsDTO(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<ApplyJobResponseDTO> appliedJobs = jobApplicationService.getAppliedJobByUserDTO(user.getId());
        return ResponseEntity.ok(appliedJobs);
    }

    // 5. Get applied jobs as entities (raw)
    @GetMapping("/my-applied-entities")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ApplyJobDto>> getAppliedJobEntities(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<ApplyJobDto> appliedJobs = jobApplicationService.getApplicationsByUser(user.getId());
        return ResponseEntity.ok(appliedJobs);
    }

    // 6. Cancel application
    @DeleteMapping("/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> cancelApplication(Authentication authentication, @RequestParam Long jobId) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String result = jobApplicationService.cancelApplication(user.getId(), jobId);
        return ResponseEntity.ok(result);
    }

    // 7. Admin: Get all applications with profiles
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<List<ApplicationProfileDtoAdmin>>> getAllApplicationsWithProfiles() {
        return jobApplicationService.getAllApplicationsWithProfiles()
                .thenApply(ResponseEntity::ok);
    }

    // 8. Admin: Get applications for a specific job
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApplyJobDto>> getApplicationsForJob(@PathVariable Long jobId) {
        List<ApplyJobDto> applications = jobApplicationService.getApplicationsForJob(jobId);
        return ResponseEntity.ok(applications);
    }
}
