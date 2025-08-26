package com.pravin.job_portal_backend.controller.company;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pravin.job_portal_backend.dto.company_dtos.*;
import com.pravin.job_portal_backend.service.company.CompanyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/propose/{recruiterId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public CompanyResponseDTO proposeCompany(@PathVariable Long recruiterId, @RequestBody CompanyCreateDTO dto) {
        return companyService.createCompanyAsRecruiter(recruiterId, dto);
    }

    @PatchMapping("/{companyId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public CompanyResponseDTO approve(@PathVariable Long companyId, @RequestParam Long adminUserId) {
        return companyService.approveCompany(companyId, adminUserId);
    }

    @PatchMapping("/{companyId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public CompanyResponseDTO reject(@PathVariable Long companyId,
            @RequestParam Long adminUserId,
            @RequestParam(required = false) String reason) {
        return companyService.rejectCompany(companyId, adminUserId, reason);
    }

    @PatchMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public CompanyResponseDTO update(@PathVariable Long companyId,
            @RequestParam Long actorUserId,
            @RequestBody CompanyUpdateDTO dto) {
        return companyService.updateCompany(companyId, actorUserId, dto);
    }

    @GetMapping("/{id}")
    public CompanyResponseDTO getOne(@PathVariable Long id) {
        return companyService.getCompany(id);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CompanyResponseDTO> pending() {
        return companyService.listPending();
    }

    @GetMapping("/approved")
    public List<CompanyResponseDTO> approved() {
        return companyService.listApproved();
    }

    @GetMapping("/recruiter/{recruiterId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    public List<CompanyResponseDTO> byRecruiter(@PathVariable Long recruiterId) {
        return companyService.listByRecruiter(recruiterId);
    }

    @DeleteMapping("/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long companyId, @RequestParam Long adminUserId) {
        companyService.deleteCompany(companyId, adminUserId);
    }

    @GetMapping("/search")
    public List<CompanyResponseDTO> search(@RequestParam String keyword) {
        return companyService.searchCompanies(keyword);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CompanyResponseDTO> all() {
        return companyService.listAll();
    }

}
