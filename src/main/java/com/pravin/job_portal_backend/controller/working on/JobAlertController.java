// package com.pravin.job_portal_backend.controller;

// import com.pravin.job_portal_backend.entity.JobAlert;
// import com.pravin.job_portal_backend.entity.User;
// import com.pravin.job_portal_backend.repository.UserRepository;
// import com.pravin.job_portal_backend.service.service_implementation.JobAlertService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/job-alerts")
// public class JobAlertController {
//     @Autowired
//     private JobAlertService jobAlertService;
//     @Autowired
//     private UserRepository userRepository;

//     @GetMapping("/user/{userId}")
//     public ResponseEntity<List<JobAlert>> getAlerts(@PathVariable Long userId) {
//         User user = userRepository.findById(userId).orElseThrow();
//         return ResponseEntity.ok(jobAlertService.getAlertsByUser(user));
//     }

//     @PostMapping("/create")
//     public ResponseEntity<JobAlert> createAlert(@RequestParam Long userId, @RequestParam String criteria) {
//         User user = userRepository.findById(userId).orElseThrow();
//         return ResponseEntity.ok(jobAlertService.createAlert(user, criteria));
//     }

//     @DeleteMapping("/delete/{alertId}")
//     public ResponseEntity<?> deleteAlert(@PathVariable Long alertId) {
//         jobAlertService.deleteAlert(alertId);
//         return ResponseEntity.ok().build();
//     }
// }
