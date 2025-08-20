package com.pravin.job_portal_backend.service.company;

import com.pravin.job_portal_backend.dto.company_dtos.CompanyRequestDTO;
import com.pravin.job_portal_backend.dto.company_dtos.CompanyResponseDTO;

import java.util.List;

public interface CompanyService {
    CompanyResponseDTO createCompany(CompanyRequestDTO companyDTO);

    CompanyResponseDTO getCompanyById(Long id);

    List<CompanyResponseDTO> getAllCompanies();

    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO companyDTO);

    void deleteCompany(Long id);
}
