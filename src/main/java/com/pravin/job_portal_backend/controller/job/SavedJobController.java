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
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<SavedJobDto>> getSavedJobsByUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(savedJobService.getSavedJobsByUser(userId));
//    }
//
//    @PostMapping("/save")
//    public ResponseEntity<SavedJobDto> saveJob(@RequestParam Long userId, @RequestParam Long jobId) {
//        return ResponseEntity.ok(savedJobService.saveJob(userId, jobId));
//    }
//
//    @DeleteMapping("/unsave")
//    public ResponseEntity<?> unsaveJob(@RequestParam Long userId, @RequestParam Long jobId) {
//        savedJobService.unsaveJob(userId, jobId);
//        return ResponseEntity.ok().build();
//    }
//}
