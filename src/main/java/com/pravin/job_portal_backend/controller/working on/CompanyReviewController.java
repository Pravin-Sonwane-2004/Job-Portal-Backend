// package com.pravin.job_portal_backend.controller;

// import com.pravin.job_portal_backend.entity.Company;
// import com.pravin.job_portal_backend.entity.CompanyReview;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import com.pravin.job_portal_backend.repository.CompanyRepository;
// import com.pravin.job_portal_backend.repository.JOB_SEEKERRepository;
// import com.pravin.job_portal_backend.service.service_implementation.CompanyReviewService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/company-reviews")
// public class CompanyReviewController {
//     @Autowired
//     private CompanyReviewService companyReviewService;
//     @Autowired
//     private CompanyRepository companyRepository;
//     @Autowired
//     private JOB_SEEKERRepository JOB_SEEKERRepository;

//     @GetMapping("/company/{companyId}")
//     public ResponseEntity<List<CompanyReview>> getReviewsByCompany(@PathVariable Long companyId) {
//         Company company = companyRepository.findById(companyId).orElseThrow();
//         return ResponseEntity.ok(companyReviewService.getReviewsByCompany(company));
//     }

//     @PostMapping("/add")
//     public ResponseEntity<CompanyReview> addReview(@RequestParam Long companyId, @RequestParam Long JOB_SEEKERId, @RequestParam String content, @RequestParam int rating) {
//         Company company = companyRepository.findById(companyId).orElseThrow();
//         JOB_SEEKER JOB_SEEKER = JOB_SEEKERRepository.findById(JOB_SEEKERId).orElseThrow();
//         return ResponseEntity.ok(companyReviewService.addReview(company, JOB_SEEKER, content, rating));
//     }

//     @DeleteMapping("/delete/{reviewId}")
//     public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
//         companyReviewService.deleteReview(reviewId);
//         return ResponseEntity.ok().build();
//     }
// }
