package com.pravin.job_portal_backend.service.company;


import com.pravin.job_portal_backend.dto.company_dtos.CompanyDTO;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.exception.ResourceNotFoundException;
import com.pravin.job_portal_backend.filter.CompanyMapper;
import com.pravin.job_portal_backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return CompanyMapper.toDTO(company);
    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        companyRepository.delete(company);
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = CompanyMapper.toEntity(companyDTO); // Convert DTO → Entity
        Company savedCompany = companyRepository.save(company); // Save in DB
        return CompanyMapper.toDTO(savedCompany); // Convert Entity → DTO
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));

        // Update fields (only if provided in DTO)
        existingCompany.setName(companyDTO.getName());
        existingCompany.setDescription(companyDTO.getDescription());
        existingCompany.setWebsite(companyDTO.getWebsite());
        existingCompany.setLocation(companyDTO.getLocation());
        existingCompany.setIndustry(companyDTO.getIndustry());
        existingCompany.setContactEmail(companyDTO.getContactEmail());

        Company updatedCompany = companyRepository.save(existingCompany);
        return CompanyMapper.toDTO(updatedCompany);
    }

}
