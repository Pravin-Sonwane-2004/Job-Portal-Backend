// package com.pravin.job_portal_backend.controller;

// import com.pravin.job_portal_backend.entity.JobAlert;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import com.pravin.job_portal_backend.repository.JOB_SEEKERRepository;
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
//     private JOB_SEEKERRepository JOB_SEEKERRepository;

//     @GetMapping("/JOB_SEEKER/{JOB_SEEKERId}")
//     public ResponseEntity<List<JobAlert>> getAlerts(@PathVariable Long JOB_SEEKERId) {
//         JOB_SEEKER JOB_SEEKER = JOB_SEEKERRepository.findById(JOB_SEEKERId).orElseThrow();
//         return ResponseEntity.ok(jobAlertService.getAlertsByJOB_SEEKER(JOB_SEEKER));
//     }

//     @PostMapping("/create")
//     public ResponseEntity<JobAlert> createAlert(@RequestParam Long JOB_SEEKERId, @RequestParam String criteria) {
//         JOB_SEEKER JOB_SEEKER = JOB_SEEKERRepository.findById(JOB_SEEKERId).orElseThrow();
//         return ResponseEntity.ok(jobAlertService.createAlert(JOB_SEEKER, criteria));
//     }

//     @DeleteMapping("/delete/{alertId}")
//     public ResponseEntity<?> deleteAlert(@PathVariable Long alertId) {
//         jobAlertService.deleteAlert(alertId);
//         return ResponseEntity.ok().build();
//     }
// }
