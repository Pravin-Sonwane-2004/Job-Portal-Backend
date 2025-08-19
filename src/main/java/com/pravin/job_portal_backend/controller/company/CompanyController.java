package com.pravin.job_portal_backend.controller.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pravin.job_portal_backend.dto.company_dtos.CompanyDTO;
import com.pravin.job_portal_backend.service.company.CompanyServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/companies")
@Validated
public class CompanyController {

    @Autowired
    private CompanyServiceImpl companyService;

    /**
     * Get company by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        CompanyDTO company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    /**
     * Get all companies with pagination.
    //  */
    // @GetMapping
    // public ResponseEntity<Page<CompanyDTO>> getAllCompanies(Pageable pageable) {
    //     Page<CompanyDTO> companies = companyService.getAllCompanies(pageable);
    //     return ResponseEntity.ok(companies);
    // }

    /**
     * Create a new company.
     */
    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        CompanyDTO created = companyService.createCompany(companyDTO);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * Update an existing company.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyDTO companyDTO) {
        CompanyDTO updated = companyService.updateCompany(id, companyDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a company by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}

// Suggestion: Add @ControllerAdvice for global exception handling for
// consistent error responses.
