package com.pravin.job_portal_backend.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.job_portal_backend.dto.CompanyDashboardDto;
import com.pravin.job_portal_backend.dto.CompanyDto;
import com.pravin.job_portal_backend.dto.CompanyEmployeeRequest;
import com.pravin.job_portal_backend.dto.CompanySignupRequest;
import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.enums.UserStatus;
import com.pravin.job_portal_backend.mapper.CompanyMapper;
import com.pravin.job_portal_backend.mapper.JobMapper;
import com.pravin.job_portal_backend.mapper.UserMapper;
import com.pravin.job_portal_backend.repository.CompanyRepository;
import com.pravin.job_portal_backend.repository.JobApply;
import com.pravin.job_portal_backend.repository.JobsRepository;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.CompanyPortalService;

@Service
public class CompanyPortalServiceImpl implements CompanyPortalService {

  private final CompanyRepository companyRepository;
  private final UserRepository userRepository;
  private final JobsRepository jobsRepository;
  private final JobApply jobApplyRepository;
  private final PasswordEncoder passwordEncoder;

  public CompanyPortalServiceImpl(
      CompanyRepository companyRepository,
      UserRepository userRepository,
      JobsRepository jobsRepository,
      JobApply jobApplyRepository,
      PasswordEncoder passwordEncoder) {
    this.companyRepository = companyRepository;
    this.userRepository = userRepository;
    this.jobsRepository = jobsRepository;
    this.jobApplyRepository = jobApplyRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public CompanyDto signupCompany(CompanySignupRequest request) {
    companyRepository.findByNameIgnoreCase(request.companyName()).ifPresent(company -> {
      throw new IllegalArgumentException("Company name is already registered.");
    });
    userRepository.findByEmail(request.ownerEmail()).ifPresent(user -> {
      throw new IllegalArgumentException("Owner email is already registered.");
    });

    Company company = Company.builder()
        .name(request.companyName())
        .description(request.description())
        .website(request.website())
        .industry(request.industry())
        .location(request.location())
        .logoUrl(request.logoUrl())
        .build();
    Company savedCompany = companyRepository.save(company);

    User owner = User.builder()
        .name(request.ownerName())
        .email(request.ownerEmail())
        .password(passwordEncoder.encode(request.password()))
        .role(Role.COMPANY_ADMIN)
        .designation("Company Admin")
        .company(savedCompany)
        .status(UserStatus.ACTIVE)
        .isDeleted(false)
        .verified(true)
        .build();
    userRepository.save(owner);
    return toDto(savedCompany);
  }

  @Override
  public CompanyDashboardDto dashboard(String email) {
    Company company = companyFor(email);
    long employees = userRepository.countByCompany(company);
    long jobs = jobsRepository.countByCompanyIgnoreCase(company.getName());
    long applications = jobApplyRepository.countByJob_CompanyIgnoreCase(company.getName());
    String nextAction = employees <= 1 ? "Add employees so your hiring team can collaborate."
        : jobs == 0 ? "Post your first job from the company portal."
            : applications == 0 ? "Share your open jobs to attract candidates."
                : "Review applications and schedule interviews.";
    return new CompanyDashboardDto(toDto(company), employees, jobs, applications, nextAction);
  }

  @Override
  public CompanyDto myCompany(String email) {
    return toDto(companyFor(email));
  }

  @Override
  @Transactional
  public CompanyDto updateMyCompany(String email, CompanyDto request) {
    User current = userFor(email);
    requireCompanyAdmin(current);
    Company company = current.getCompany();
    company.setDescription(request.description());
    company.setWebsite(request.website());
    company.setIndustry(request.industry());
    company.setLocation(request.location());
    company.setLogoUrl(request.logoUrl());
    return toDto(companyRepository.save(company));
  }

  @Override
  public List<UserDto> employees(String email) {
    Company company = companyFor(email);
    return userRepository.findByCompany(company).stream().map(UserMapper::toDto).toList();
  }

  @Override
  @Transactional
  public UserDto addEmployee(String email, CompanyEmployeeRequest request) {
    User current = userFor(email);
    requireCompanyAdmin(current);
    userRepository.findByEmail(request.email()).ifPresent(user -> {
      throw new IllegalArgumentException("Employee email is already registered.");
    });

    User employee = User.builder()
        .name(request.name())
        .email(request.email())
        .password(passwordEncoder.encode(request.password()))
        .role(Role.COMPANY_EMPLOYEE)
        .designation(request.designation())
        .phoneNumber(request.phoneNumber())
        .company(current.getCompany())
        .status(UserStatus.ACTIVE)
        .isDeleted(false)
        .verified(true)
        .build();
    return UserMapper.toDto(userRepository.save(employee));
  }

  @Override
  @Transactional
  public void removeEmployee(String email, Long employeeId) {
    User current = userFor(email);
    requireCompanyAdmin(current);
    User employee = userRepository.findById(employeeId)
        .orElseThrow(() -> new IllegalArgumentException("Employee not found."));
    if (employee.getCompany() == null || !employee.getCompany().getId().equals(current.getCompany().getId())) {
      throw new IllegalArgumentException("Employee does not belong to your company.");
    }
    if (employee.getId().equals(current.getId())) {
      throw new IllegalArgumentException("Company admin cannot remove their own account.");
    }
    userRepository.delete(employee);
  }

  @Override
  public List<JobDto> jobs(String email) {
    Company company = companyFor(email);
    return jobsRepository.findByCompanyIgnoreCase(company.getName()).stream().map(JobMapper::toDto).toList();
  }

  @Override
  @Transactional
  public JobDto createJob(String email, JobDto request) {
    User current = userFor(email);
    Company company = requireCompanyUser(current);
    Job job = JobMapper.toEntity(request);
    job.setPostedBy(current);
    job.setCompany(company.getName());
    return JobMapper.toDto(jobsRepository.save(job));
  }

  @Override
  @Transactional
  public JobDto updateJob(String email, Long jobId, JobDto request) {
    User current = userFor(email);
    Company company = requireCompanyUser(current);
    Job job = companyJob(jobId, company);
    applyJobUpdates(job, request);
    job.setCompany(company.getName());
    return JobMapper.toDto(jobsRepository.save(job));
  }

  @Override
  @Transactional
  public void deleteJob(String email, Long jobId) {
    User current = userFor(email);
    Company company = requireCompanyUser(current);
    Job job = companyJob(jobId, company);
    jobsRepository.delete(job);
  }

  private User userFor(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found."));
  }

  private Company companyFor(String email) {
    return requireCompanyUser(userFor(email));
  }

  private Company requireCompanyUser(User user) {
    if (user.getCompany() == null
        || !(Role.COMPANY_ADMIN.equals(user.getRole()) || Role.COMPANY_EMPLOYEE.equals(user.getRole()))) {
      throw new IllegalArgumentException("Company account is required.");
    }
    return user.getCompany();
  }

  private void requireCompanyAdmin(User user) {
    if (!Role.COMPANY_ADMIN.equals(user.getRole()) || user.getCompany() == null) {
      throw new IllegalArgumentException("Company admin access is required.");
    }
  }

  private Job companyJob(Long jobId, Company company) {
    Job job = jobsRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found."));
    if (job.getCompany() == null || !job.getCompany().equalsIgnoreCase(company.getName())) {
      throw new IllegalArgumentException("Job does not belong to your company.");
    }
    return job;
  }

  private void applyJobUpdates(Job job, JobDto request) {
    if (request.getTitle() != null) job.setTitle(request.getTitle());
    if (request.getLocation() != null) job.setLocation(request.getLocation());
    if (request.getSalary() != null) job.setSalary(request.getSalary());
    if (request.getDescription() != null) job.setDescription(request.getDescription());
    if (request.getRequirements() != null) job.setRequirements(request.getRequirements());
    if (request.getJobType() != null) job.setJobType(request.getJobType());
    if (request.getExperienceLevel() != null) job.setExperienceLevel(request.getExperienceLevel());
    if (request.getStatus() != null) job.setStatus(request.getStatus());
    if (request.getCategory() != null) job.setCategory(request.getCategory());
    if (request.getLastDateToApply() != null) job.setLastDateToApply(request.getLastDateToApply());
  }

  private CompanyDto toDto(Company company) {
    return CompanyMapper.toDto(
        company,
        userRepository.countByCompany(company),
        jobsRepository.countByCompanyIgnoreCase(company.getName()));
  }
}
