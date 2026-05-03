package com.pravin.job_portal_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.CompanyReviewDto;
import com.pravin.job_portal_backend.dto.CreateCompanyReviewRequest;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.CompanyReview;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.CompanyRepository;
import com.pravin.job_portal_backend.repository.CompanyReviewRepository;
import com.pravin.job_portal_backend.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/company-reviews")
public class CompanyReviewController {

  private final CompanyReviewRepository reviewRepository;
  private final CompanyRepository companyRepository;
  private final UserRepository userRepository;

  public CompanyReviewController(CompanyReviewRepository reviewRepository, CompanyRepository companyRepository,
      UserRepository userRepository) {
    this.reviewRepository = reviewRepository;
    this.companyRepository = companyRepository;
    this.userRepository = userRepository;
  }

  @GetMapping("/company/{companyId}")
  public ResponseEntity<List<CompanyReviewDto>> getReviews(@PathVariable Long companyId) {
    Company company = companyRepository.findById(companyId)
        .orElseThrow(() -> new IllegalArgumentException("Company not found."));
    return ResponseEntity.ok(reviewRepository.findByCompanyOrderByCreatedAtDesc(company).stream().map(this::toDto).toList());
  }

  @PostMapping("/company/{companyId}")
  public ResponseEntity<CompanyReviewDto> addReview(Authentication authentication, @PathVariable Long companyId,
      @Valid @RequestBody CreateCompanyReviewRequest request) {
    Company company = companyRepository.findById(companyId)
        .orElseThrow(() -> new IllegalArgumentException("Company not found."));
    User user = userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
    CompanyReview review = CompanyReview.builder()
        .company(company)
        .user(user)
        .content(request.content())
        .rating(request.rating())
        .build();
    return ResponseEntity.status(HttpStatus.CREATED).body(toDto(reviewRepository.save(review)));
  }

  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
    reviewRepository.deleteById(reviewId);
    return ResponseEntity.noContent().build();
  }

  private CompanyReviewDto toDto(CompanyReview review) {
    return new CompanyReviewDto(
        review.getId(),
        review.getCompany().getId(),
        review.getCompany().getName(),
        review.getUser().getId(),
        review.getUser().getName(),
        review.getContent(),
        review.getRating(),
        review.getCreatedAt());
  }
}
