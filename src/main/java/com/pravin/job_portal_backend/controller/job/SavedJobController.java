package com.pravin.job_portal_backend.controller.job;
//package com.pravin.job_portal_backend.controller;
//
//import com.pravin.job_portal_backend.dto.SavedJobDto;
//import com.pravin.job_portal_backend.service.interfaces.SavedJobService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/saved-jobs")
//public class SavedJobController {
//    @Autowired
//    private SavedJobService savedJobService;
//
//    @GetMapping("/JOB_SEEKER/{JOB_SEEKERId}")
//    public ResponseEntity<List<SavedJobDto>> getSavedJobsByJOB_SEEKER(@PathVariable Long JOB_SEEKERId) {
//        return ResponseEntity.ok(savedJobService.getSavedJobsByJOB_SEEKER(JOB_SEEKERId));
//    }
//
//    @PostMapping("/save")
//    public ResponseEntity<SavedJobDto> saveJob(@RequestParam Long JOB_SEEKERId, @RequestParam Long jobId) {
//        return ResponseEntity.ok(savedJobService.saveJob(JOB_SEEKERId, jobId));
//    }
//
//    @DeleteMapping("/unsave")
//    public ResponseEntity<?> unsaveJob(@RequestParam Long JOB_SEEKERId, @RequestParam Long jobId) {
//        savedJobService.unsaveJob(JOB_SEEKERId, jobId);
//        return ResponseEntity.ok().build();
//    }
//}
