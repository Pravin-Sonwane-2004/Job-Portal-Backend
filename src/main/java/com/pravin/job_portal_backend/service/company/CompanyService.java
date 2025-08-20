package com.pravin.job_portal_backend.service.company;

import java.util.List;

import com.pravin.job_portal_backend.dto.company_dtos.CompanyDTO;

public interface CompanyService {
    CompanyDTO createCompany(CompanyDTO companyDTO);

    CompanyDTO getCompanyById(Long id);

    List<CompanyDTO> getAllCompanies();

    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);

    void deleteCompany(Long id);
}
