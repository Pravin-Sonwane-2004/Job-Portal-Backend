//package com.pravin.job_portal_backend.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.pravin.job_portal_backend.dto.JobDto;
//import com.pravin.job_portal_backend.service.interfaces.JobService;
//
//@RestController
//@RequestMapping("/user")
//public class JobController {
//
//  @Autowired
//  private JobService jobService;
//
//  // @GetMapping("/jobs/paginated")
//  // public ResponseEntity<?> getAllJobsPaginated(
//  // @RequestParam(defaultValue = "0") int page,
//  // @RequestParam(defaultValue = "10") int size,
//  // @RequestParam(defaultValue = "postedDate") String sortBy,
//  // @RequestParam(defaultValue = "asc") String sortDir,
//  // @RequestParam(required = false) String jobTitle,
//  // @RequestParam(required = false) String jobLocation,
//  // @RequestParam(required = false) Double minSalary,
//  // @RequestParam(required = false) Double maxSalary) {
//  // try {
//  // // Validate sortBy
//  // List<String> allowedSortFields = List.of("postedDate", "jobSalary",
//  // "jobTitle", "jobLocation", "id");
//  // if (!allowedSortFields.contains(sortBy)) {
//  // sortBy = "postedDate";
//  // }
//  // // Pass all filter params to the service for query
//  // org.springframework.data.domain.Page<com.pravin.job_portal_backend.entity.jobs>
//  // pageResult = jobService
//  // .getAllJobsOfPagable(page, size, sortBy, sortDir, jobTitle, jobLocation,
//  // minSalary, maxSalary);
//
//  // // Build response map
//  // java.util.Map<String, Object> response = new java.util.HashMap<>();
//  // response.put("content", pageResult.getContent());
//  // response.put("totalPages", pageResult.getTotalPages());
//  // response.put("totalElements", pageResult.getTotalElements());
//  // response.put("pageNumber", pageResult.getNumber());
//  // response.put("pageSize", pageResult.getSize());
//
//  // return ResponseEntity.ok(response);
//  // } catch (RuntimeException e) {
//  // return
//  // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//  // }
//  // }
//
//  @GetMapping("/jobs")
//  public ResponseEntity<List<JobDto>> getAllJobs() {
//    List<JobDto> jobs = jobService.getAllJobs();
//    return new ResponseEntity<>(jobs, HttpStatus.OK);
//  }
//
//  @GetMapping("/jobs/{id}")
//  public ResponseEntity<JobDto> getJobById(@PathVariable Long id) {
//    try {
//      JobDto job = jobService.getJobById(id);
//      return new ResponseEntity<>(job, HttpStatus.OK);
//    } catch (RuntimeException e) {
//      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//  }
//
//  @GetMapping("/jobs/sorted")
//  public ResponseEntity<?> getAllJobsSortedPaginated(
//      @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int page,
//      @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int size,
//      @org.springframework.web.bind.annotation.RequestParam(defaultValue = "id") String sortBy,
//      @org.springframework.web.bind.annotation.RequestParam(defaultValue = "asc") String direction) {
//    try {
//      return ResponseEntity.ok(jobService.getAllJobs());
//    } catch (RuntimeException e) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//    }
//  }
//}