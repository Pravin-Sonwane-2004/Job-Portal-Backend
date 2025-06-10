// package com.pravin.job_portal_backend.service;
// package com.pravin.job_portal_backend.service.service_implementation;

// import com.pravin.job_portal_backend.entity.Company;
// import com.pravin.job_portal_backend.repository.CompanyRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class CompanyService {
//     @Autowired
//     private CompanyRepository companyRepository;

//     public List<Company> getAllCompanies() {
//         return companyRepository.findAll();
//     }

//     public Optional<Company> getCompanyById(Long id) {
//         return companyRepository.findById(id);
//     }

//     public Optional<Company> getCompanyByName(String name) {
//         return companyRepository.findByName(name);
//     }

//     public Company createOrUpdateCompany(Company company) {
//         return companyRepository.save(company);
//     }

//     public void deleteCompany(Long id) {
//         companyRepository.deleteById(id);
//     }
// }
