package com.pravin.job_portal_backend.service.interfaces;

import java.util.List;

import com.pravin.job_portal_backend.dto.CompanyDashboardDto;
import com.pravin.job_portal_backend.dto.CompanyDto;
import com.pravin.job_portal_backend.dto.CompanyEmployeeRequest;
import com.pravin.job_portal_backend.dto.CompanySignupRequest;
import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.dto.UserDto;

public interface CompanyPortalService {
  CompanyDto signupCompany(CompanySignupRequest request);

  CompanyDashboardDto dashboard(String email);

  CompanyDto myCompany(String email);

  CompanyDto updateMyCompany(String email, CompanyDto request);

  List<UserDto> employees(String email);

  UserDto addEmployee(String email, CompanyEmployeeRequest request);

  void removeEmployee(String email, Long employeeId);

  List<JobDto> jobs(String email);

  JobDto createJob(String email, JobDto request);
}
