package com.pravin.job_portal_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.CompanyDto;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.repository.CompanyRepository;
import com.pravin.job_portal_backend.repository.JobsRepository;
import com.pravin.job_portal_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

  private final CompanyRepository companyRepository;
  private final UserRepository userRepository;
  private final JobsRepository jobsRepository;

  public CompanyController(CompanyRepository companyRepository, UserRepository userRepository,
      JobsRepository jobsRepository) {
    this.companyRepository = companyRepository;
    this.userRepository = userRepository;
    this.jobsRepository = jobsRepository;
  }

  @GetMapping
  public ResponseEntity<List<CompanyDto>> getCompanies() {
    return ResponseEntity.ok(companyRepository.findAll().stream().map(this::toDto).toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CompanyDto> getCompany(@PathVariable Long id) {
    return companyRepository.findById(id).map(this::toDto).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  private CompanyDto toDto(Company company) {
    return new CompanyDto(
        company.getId(),
        company.getName(),
        company.getDescription(),
        company.getWebsite(),
        company.getIndustry(),
        company.getLocation(),
        company.getLogoUrl(),
        company.isVerified(),
        userRepository.countByCompany(company),
        jobsRepository.countByCompanyIgnoreCase(company.getName()));
  }
}
