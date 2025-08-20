package com.pravin.job_portal_backend.controller.recruiter;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pravin.job_portal_backend.dto.ricruitors_dtos.RecruiterDTO;
import com.pravin.job_portal_backend.service.recruiter.RecruiterService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;

    @PostMapping
    public ResponseEntity<RecruiterDTO> createRecruiter(@RequestBody RecruiterDTO recruiterDTO) {
        return ResponseEntity.ok(recruiterService.createRecruiter(recruiterDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruiterDTO> updateRecruiter(@PathVariable Long id, @RequestBody RecruiterDTO recruiterDTO) {
        return ResponseEntity.ok(recruiterService.updateRecruiter(id, recruiterDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruiterDTO> getRecruiterById(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.getRecruiterById(id));
    }

    @GetMapping
    public ResponseEntity<List<RecruiterDTO>> getAllRecruiters() {
        return ResponseEntity.ok(recruiterService.getAllRecruiters());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruiter(@PathVariable Long id) {
        recruiterService.deleteRecruiter(id);
        return ResponseEntity.noContent().build();
    }
}
