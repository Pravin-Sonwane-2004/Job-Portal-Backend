// package com.pravin.job_portal_backend.service;
// package com.pravin.job_portal_backend.service.service_implementation;

// import com.pravin.job_portal_backend.entity.Company;
// import com.pravin.job_portal_backend.entity.CompanyReview;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import com.pravin.job_portal_backend.repository.CompanyReviewRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class CompanyReviewService {
//     @Autowired
//     private CompanyReviewRepository companyReviewRepository;

//     public List<CompanyReview> getReviewsByCompany(Company company) {
//         return companyReviewRepository.findByCompany(company);
//     }

//     public CompanyReview addReview(Company company, JOB_SEEKER JOB_SEEKER, String content, int rating) {
//         return companyReviewRepository.save(new CompanyReview(company, JOB_SEEKER, content, rating));
//     }

//     public void deleteReview(Long reviewId) {
//         companyReviewRepository.deleteById(reviewId);
//     }
// }
