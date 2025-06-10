package com.pravin.job_portal_backend.controller;

import com.pravin.job_portal_backend.dto.ResumeDto;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.ResumeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResumeDto>> getResumes(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(resumeService.getResumesByUser(user));
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeDto> uploadResume(@RequestParam Long userId, @RequestParam String filePath) {
        User user = userRepository.findById(userId).orElseThrow();
        // In a real app, handle file upload and pass the file path
        return ResponseEntity.ok(resumeService.uploadResume(user, filePath));
    }

    @DeleteMapping("/delete/{resumeId}")
    public ResponseEntity<?> deleteResume(@PathVariable Long resumeId) {
        resumeService.deleteResume(resumeId);
        return ResponseEntity.ok().build();
    }
}
