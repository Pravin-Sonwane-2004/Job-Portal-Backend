package com.pravin.job_portal_backend.controller.company;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pravin.job_portal_backend.dto.company_dtos.CompanyRequestDTO;
import com.pravin.job_portal_backend.dto.company_dtos.CompanyResponseDTO;
import com.pravin.job_portal_backend.service.company.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // CREATE company
    @PostMapping
    public ResponseEntity<CompanyResponseDTO> createCompany(@RequestBody CompanyRequestDTO companyDTO) {
        return ResponseEntity.ok(companyService.createCompany(companyDTO));
    }

    // UPDATE company
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(@PathVariable Long id,
            @RequestBody CompanyRequestDTO companyDTO) {
        return ResponseEntity.ok(companyService.updateCompany(id, companyDTO));
    }

    // GET company by ID
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    // GET all companies
    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    // DELETE company
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
