package com.pravin.job_portal_backend.controller.recruiter;


import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pravin.job_portal_backend.dto.ricruitors_dtos.RecruiterResponseDTO;
import com.pravin.job_portal_backend.service.recruiter.RecruiterService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RecruiterResponseDTO create(@RequestParam Long userId) {
        return recruiterService.createRecruiter(userId);
    }

    @PatchMapping("/{recruiterId}/assign/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public RecruiterResponseDTO assign(@PathVariable Long recruiterId,
            @PathVariable Long companyId,
            @RequestParam Long adminUserId) {
        return recruiterService.assignRecruiterToCompany(recruiterId, companyId, adminUserId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public RecruiterResponseDTO get(@PathVariable Long id) {
        return recruiterService.getRecruiter(id);
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public List<RecruiterResponseDTO> byCompany(@PathVariable Long companyId) {
        return recruiterService.listByCompany(companyId);
    }

    @PatchMapping("/{recruiterId}/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public RecruiterResponseDTO removeFromCompany(@PathVariable Long recruiterId,
            @RequestParam Long adminUserId) {
        return recruiterService.removeRecruiterFromCompany(recruiterId, adminUserId);
    }

    @DeleteMapping("/{recruiterId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRecruiter(@PathVariable Long recruiterId,
            @RequestParam Long adminUserId) {
        recruiterService.deleteRecruiter(recruiterId, adminUserId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RecruiterResponseDTO> allRecruiters() {
        return recruiterService.listAllRecruiters();
    }

}
