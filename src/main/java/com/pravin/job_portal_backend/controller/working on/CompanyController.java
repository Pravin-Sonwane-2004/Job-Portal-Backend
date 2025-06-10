// package com.pravin.job_portal_backend.controller;

// import com.pravin.job_portal_backend.entity.Company;
// import com.pravin.job_portal_backend.service.service_implementation.CompanyService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/companies")
// public class CompanyController {
//     @Autowired
//     private CompanyService companyService;

//     @GetMapping("")
//     public ResponseEntity<List<Company>> getAllCompanies() {
//         return ResponseEntity.ok(companyService.getAllCompanies());
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
//         return companyService.getCompanyById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     @PostMapping("/create-or-update")
//     public ResponseEntity<Company> createOrUpdateCompany(@RequestBody Company company) {
//         return ResponseEntity.ok(companyService.createOrUpdateCompany(company));
//     }

//     @DeleteMapping("/delete/{id}")
//     public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
//         companyService.deleteCompany(id);
//         return ResponseEntity.ok().build();
//     }
// }
