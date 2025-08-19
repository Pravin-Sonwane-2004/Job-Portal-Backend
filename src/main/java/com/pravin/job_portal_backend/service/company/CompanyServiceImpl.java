package com.pravin.job_portal_backend.service.company;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.dto.company_dtos.CompanyDTO;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.exception.ResourceNotFoundException;
import com.pravin.job_portal_backend.mapper.companys_mapper.CompanyMapper;
import com.pravin.job_portal_backend.repository.CompanyRepository;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public CompanyDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        return CompanyMapper.toDTO(company);
    }

    @Override
    public List<CompanyDTO> getAllCompanies(Pageable pageable) {
        List<Company> companies = companyRepository.findAll(pageable).getContent();
        return companies.stream()
                .map(CompanyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = CompanyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);
        return CompanyMapper.toDTO(company);
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        company.setName(companyDTO.getName());
        company.setDescription(companyDTO.getDescription());
        company.setWebsite(companyDTO.getWebsite());
        company.setLocation(companyDTO.getLocation());
        company.setIndustry(companyDTO.getIndustry());
        company.setContactEmail(companyDTO.getContactEmail());
        company = companyRepository.save(company);
        return CompanyMapper.toDTO(company);
    }

    @Override
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found");
        }
        companyRepository.deleteById(id);
    }
}
