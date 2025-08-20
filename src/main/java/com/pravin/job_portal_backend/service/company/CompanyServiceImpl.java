package com.pravin.job_portal_backend.service.company;

import com.pravin.job_portal_backend.dto.company_dtos.CompanyRequestDTO;
import com.pravin.job_portal_backend.dto.company_dtos.CompanyResponseDTO;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.exception.ResourceNotFoundException;
import com.pravin.job_portal_backend.mapper.company_mapper.CompanyMapper;
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
    public CompanyResponseDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return CompanyMapper.toResponseDto(company);
    }

    @Override
    public List<CompanyResponseDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        companyRepository.delete(company);
    }

    @Override
    public CompanyResponseDTO createCompany(CompanyRequestDTO companyDTO) {
        Company company = CompanyMapper.toEntity(companyDTO); // DTO → Entity
        Company savedCompany = companyRepository.save(company);
        return CompanyMapper.toResponseDto(savedCompany); // Entity → ResponseDTO
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO companyDTO) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        // Use mapper method instead of manual field setting
        CompanyMapper.updateEntityFromDto(companyDTO, existingCompany);

        Company updatedCompany = companyRepository.save(existingCompany);
        return CompanyMapper.toResponseDto(updatedCompany);
    }
}
