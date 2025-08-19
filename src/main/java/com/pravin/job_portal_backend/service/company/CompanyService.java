package com.pravin.job_portal_backend.service.company;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.pravin.job_portal_backend.dto.company_dtos.CompanyDTO;

public interface CompanyService {
    CompanyDTO getCompanyById(Long id);

    List<CompanyDTO> getAllCompanies(Pageable pageable);

    CompanyDTO createCompany(CompanyDTO companyDTO);

    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);

    void deleteCompany(Long id);
}
