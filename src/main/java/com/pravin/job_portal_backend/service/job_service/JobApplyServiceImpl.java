// package com.pravin.job_portal_backend.service.job_service;

// import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobDto;
// import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobResponseDTO;
// import com.pravin.job_portal_backend.dto.job_dtos.ApplicationProfileDtoAdmin;
// import com.pravin.job_portal_backend.entity.ApplyJob;
// import com.pravin.job_portal_backend.entity.Job;
// import com.pravin.job_portal_backend.entity.User;
// import com.pravin.job_portal_backend.mapper.apply_job_mapper.ApplyJobMapper;
// import com.pravin.job_portal_backend.repository.JobApply;
// import com.pravin.job_portal_backend.repository.JobsRepository;
// import com.pravin.job_portal_backend.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.CompletableFuture;
// import java.util.stream.Collectors;

// @Service
// public class JobApplyServiceImpl implements JobApplyService {

//     @Autowired
//     private JobApply jobApplicationRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private JobsRepository jobRepository;

//     @Override
//     public String applyForJob(Long userId, Long jobId) {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new RuntimeException("User not found"));
//         Job job = jobRepository.findById(jobId)
//                 .orElseThrow(() -> new RuntimeException("Job not found"));

//         if (jobApplicationRepository.existsByUserAndJob(user, job)) {
//             return "Already applied to this job.";
//         }

//         ApplyJob application = ApplyJob.builder()
//                 .user(user)
//                 .job(job)
//                 .appliedAt(LocalDateTime.now())
//                 .status("APPLIED")
//                 .build();

//         jobApplicationRepository.save(application);
//         return "Job application submitted successfully.";
//     }

//     @Override
//     public void deleteApplicationById(Long applicationId) {
//         ApplyJob application = jobApplicationRepository.findById(applicationId)
//                 .orElseThrow(() -> new RuntimeException("Application not found"));
//         jobApplicationRepository.delete(application);
//     }

//     @Override
//     public void updateApplicationById(Long applicationId, Map<String, Object> updates) {
//         ApplyJob application = jobApplicationRepository.findById(applicationId)
//                 .orElseThrow(() -> new RuntimeException("Application not found"));

//         if (updates.containsKey("status")) {
//             application.setStatus((String) updates.get("status"));
//         }
//         if (updates.containsKey("resumeLink")) {
//             application.setResumeLink((String) updates.get("resumeLink"));
//         }
//         if (updates.containsKey("coverLetter")) {
//             application.setCoverLetter((String) updates.get("coverLetter"));
//         }

//         jobApplicationRepository.save(application);
//     }

//     @Override
//     public CompletableFuture<List<ApplicationProfileDtoAdmin>> getAllApplicationsWithProfiles() {
//         List<ApplicationProfileDtoAdmin> result = jobApplicationRepository.findAll().stream()
//                 .map(app -> {
//                     Job job = app.getJob();
//                     User user = app.getUser();
//                     return ApplicationProfileDtoAdmin.builder()
//                             .applicationId(app.getId())
//                             .jobTitle(job.getTitle())
//                             .company(job.getCompany())
//                             .applicantName(user.getName())
//                             .applicantEmail(user.getEmail())
//                             .applicantProfile(user.getBio())
//                             .status(app.getStatus())
//                             .resumeLink(app.getResumeLink())
//                             .coverLetter(app.getCoverLetter())
//                             .appliedAt(app.getAppliedAt())
//                             .build();
//                 })
//                 .collect(Collectors.toList());
//         return CompletableFuture.completedFuture(result);
//     }

//     @Override
//     public List<ApplyJobDto> getAllApplications() {
//         return jobApplicationRepository.findAll().stream()
//                 .map(ApplyJobMapper::toDto)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public List<ApplyJobDto> getApplicationsForJob(Long jobId) {
//         Job job = jobRepository.findById(jobId)
//                 .orElseThrow(() -> new RuntimeException("Job not found"));

//         return jobApplicationRepository.findAll().stream()
//                 .filter(app -> app.getJob().getId().equals(jobId))
//                 .map(ApplyJobMapper::toDto)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public List<ApplyJobDto> getApplicationsByUser(Long userId) {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new RuntimeException("User not found"));

//         return jobApplicationRepository.findByUser(user).stream()
//                 .map(ApplyJobMapper::toDto)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public List<ApplyJobResponseDTO> getAppliedJobByUserDTO(Long userId) {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new RuntimeException("User not found"));

//         return jobApplicationRepository.findByUser(user).stream()
//                 .map(ApplyJobMapper::toResponseDto)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public String cancelApplication(Long userId, Long jobId) {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new RuntimeException("User not found"));
//         Job job = jobRepository.findById(jobId)
//                 .orElseThrow(() -> new RuntimeException("Job not found"));

//         ApplyJob application = jobApplicationRepository.findByUser(user).stream()
//                 .filter(app -> app.getJob().equals(job))
//                 .findFirst()
//                 .orElseThrow(() -> new RuntimeException("Application not found"));

//         jobApplicationRepository.delete(application);
//         return "Application cancelled successfully.";
//     }
// }
