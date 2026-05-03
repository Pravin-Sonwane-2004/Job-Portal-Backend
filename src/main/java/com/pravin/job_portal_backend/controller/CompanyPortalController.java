package com.pravin.job_portal_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.CompanyDashboardDto;
import com.pravin.job_portal_backend.dto.CompanyDto;
import com.pravin.job_portal_backend.dto.CompanyEmployeeRequest;
import com.pravin.job_portal_backend.dto.CompanySignupRequest;
import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.service.interfaces.CompanyPortalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping
public class CompanyPortalController {

  private final CompanyPortalService companyPortalService;

  public CompanyPortalController(CompanyPortalService companyPortalService) {
    this.companyPortalService = companyPortalService;
  }

  @PostMapping("/public/company/signup")
  public ResponseEntity<CompanyDto> signup(@Valid @RequestBody CompanySignupRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(companyPortalService.signupCompany(request));
  }

  @GetMapping("/company-portal/dashboard")
  public ResponseEntity<CompanyDashboardDto> dashboard(Authentication authentication) {
    return ResponseEntity.ok(companyPortalService.dashboard(authentication.getName()));
  }

  @GetMapping("/company-portal/company")
  public ResponseEntity<CompanyDto> myCompany(Authentication authentication) {
    return ResponseEntity.ok(companyPortalService.myCompany(authentication.getName()));
  }

  @PutMapping("/company-portal/company")
  public ResponseEntity<CompanyDto> updateCompany(Authentication authentication, @RequestBody CompanyDto request) {
    return ResponseEntity.ok(companyPortalService.updateMyCompany(authentication.getName(), request));
  }

  @GetMapping("/company-portal/employees")
  public ResponseEntity<List<UserDto>> employees(Authentication authentication) {
    return ResponseEntity.ok(companyPortalService.employees(authentication.getName()));
  }

  @PostMapping("/company-portal/employees")
  public ResponseEntity<UserDto> addEmployee(Authentication authentication,
      @Valid @RequestBody CompanyEmployeeRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(companyPortalService.addEmployee(authentication.getName(), request));
  }

  @DeleteMapping("/company-portal/employees/{employeeId}")
  public ResponseEntity<Void> removeEmployee(Authentication authentication, @PathVariable Long employeeId) {
    companyPortalService.removeEmployee(authentication.getName(), employeeId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/company-portal/jobs")
  public ResponseEntity<List<JobDto>> jobs(Authentication authentication) {
    return ResponseEntity.ok(companyPortalService.jobs(authentication.getName()));
  }

  @PostMapping("/company-portal/jobs")
  public ResponseEntity<JobDto> createJob(Authentication authentication, @RequestBody JobDto request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(companyPortalService.createJob(authentication.getName(), request));
  }
}
