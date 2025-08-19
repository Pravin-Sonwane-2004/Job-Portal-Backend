// package com.pravin.job_portal_backend.controller;

// import com.pravin.job_portal_backend.entity.Interview;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import com.pravin.job_portal_backend.entity.ApplyJob;
// import com.pravin.job_portal_backend.repository.JOB_SEEKERRepository;
// import com.pravin.job_portal_backend.repository.JobApply;
// import com.pravin.job_portal_backend.service.service_implementation.InterviewService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDateTime;
// import java.util.List;

// @RestController
// @RequestMapping("/api/interviews")
// public class InterviewController {
//     @Autowired
//     private InterviewService interviewService;
//     @Autowired
//     private JOB_SEEKERRepository JOB_SEEKERRepository;
//     @Autowired
//     private JobApply jobApplyRepository;

//     @GetMapping("/JOB_SEEKER/{JOB_SEEKERId}")
//     public ResponseEntity<List<Interview>> getInterviewsByJOB_SEEKER(@PathVariable Long JOB_SEEKERId) {
//         JOB_SEEKER JOB_SEEKER = JOB_SEEKERRepository.findById(JOB_SEEKERId).orElseThrow();
//         return ResponseEntity.ok(interviewService.getInterviewsByJOB_SEEKER(JOB_SEEKER));
//     }

//     @GetMapping("/employer/{employerId}")
//     public ResponseEntity<List<Interview>> getInterviewsByEmployer(@PathVariable Long employerId) {
//         JOB_SEEKER employer = JOB_SEEKERRepository.findById(employerId).orElseThrow();
//         return ResponseEntity.ok(interviewService.getInterviewsByEmployer(employer));
//     }

//     @GetMapping("/application/{applicationId}")
//     public ResponseEntity<List<Interview>> getInterviewsByApplication(@PathVariable Long applicationId) {
//         ApplyJob application = jobApplyRepository.findById(applicationId).orElseThrow();
//         return ResponseEntity.ok(interviewService.getInterviewsByApplication(application));
//     }

//     @PostMapping("/schedule")
//     public ResponseEntity<Interview> scheduleInterview(
//             @RequestParam Long applicationId,
//             @RequestParam Long employerId,
//             @RequestParam Long JOB_SEEKERId,
//             @RequestParam String scheduledTime,
//             @RequestParam String status) {
//         ApplyJob application = jobApplyRepository.findById(applicationId).orElseThrow();
//         JOB_SEEKER employer = JOB_SEEKERRepository.findById(employerId).orElseThrow();
//         JOB_SEEKER JOB_SEEKER = JOB_SEEKERRepository.findById(JOB_SEEKERId).orElseThrow();
//         LocalDateTime time = LocalDateTime.parse(scheduledTime);
//         return ResponseEntity.ok(interviewService.scheduleInterview(application, employer, JOB_SEEKER, time, status));
//     }

//     @PutMapping("/update-status/{interviewId}")
//     public ResponseEntity<?> updateInterviewStatus(@PathVariable Long interviewId, @RequestParam String status) {
//         interviewService.updateInterviewStatus(interviewId, status);
//         return ResponseEntity.ok().build();
//     }

//     @DeleteMapping("/delete/{interviewId}")
//     public ResponseEntity<?> deleteInterview(@PathVariable Long interviewId) {
//         interviewService.deleteInterview(interviewId);
//         return ResponseEntity.ok().build();
//     }
// }
