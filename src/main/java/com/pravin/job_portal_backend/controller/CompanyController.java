package com.pravin.job_portal_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.CompanyDto;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.mapper.CompanyMapper;
import com.pravin.job_portal_backend.repository.CompanyRepository;
import com.pravin.job_portal_backend.repository.JobsRepository;
import com.pravin.job_portal_backend.repository.UserRepository;

/**
 * Public company API.
 *
 * Flow:
 * Controller -> repositories -> CompanyDto response.
 * This controller is read-only, so the repository calls are simple enough to
 * keep here instead of adding a separate service just for listing companies.
 */
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

  /**
   * Returns all companies with dashboard-style counts used by the frontend.
   */
  @GetMapping
  public ResponseEntity<List<CompanyDto>> getCompanies() {
    return ResponseEntity.ok(companyRepository.findAll().stream().map(this::toDto).toList());
  }

  /**
   * Returns one company if it exists, otherwise Spring sends HTTP 404.
   */
  @GetMapping("/{id}")
  public ResponseEntity<CompanyDto> getCompany(@PathVariable Long id) {
    return companyRepository.findById(id).map(this::toDto).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Converts the database entity into the API response shape.
   * The extra counts are calculated here so the frontend gets one ready-to-use
   * company object.
   */
  private CompanyDto toDto(Company company) {
    return CompanyMapper.toDto(
        company,
        userRepository.countByCompany(company),
        jobsRepository.countByCompanyIgnoreCase(company.getName()));
  }
}
