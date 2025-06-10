// package com.pravin.job_portal_backend.controller;

// import com.pravin.job_portal_backend.entity.Interview;
// import com.pravin.job_portal_backend.entity.User;
// import com.pravin.job_portal_backend.entity.ApplyJob;
// import com.pravin.job_portal_backend.repository.UserRepository;
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
//     private UserRepository userRepository;
//     @Autowired
//     private JobApply jobApplyRepository;

//     @GetMapping("/user/{userId}")
//     public ResponseEntity<List<Interview>> getInterviewsByUser(@PathVariable Long userId) {
//         User user = userRepository.findById(userId).orElseThrow();
//         return ResponseEntity.ok(interviewService.getInterviewsByUser(user));
//     }

//     @GetMapping("/employer/{employerId}")
//     public ResponseEntity<List<Interview>> getInterviewsByEmployer(@PathVariable Long employerId) {
//         User employer = userRepository.findById(employerId).orElseThrow();
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
//             @RequestParam Long userId,
//             @RequestParam String scheduledTime,
//             @RequestParam String status) {
//         ApplyJob application = jobApplyRepository.findById(applicationId).orElseThrow();
//         User employer = userRepository.findById(employerId).orElseThrow();
//         User user = userRepository.findById(userId).orElseThrow();
//         LocalDateTime time = LocalDateTime.parse(scheduledTime);
//         return ResponseEntity.ok(interviewService.scheduleInterview(application, employer, user, time, status));
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
