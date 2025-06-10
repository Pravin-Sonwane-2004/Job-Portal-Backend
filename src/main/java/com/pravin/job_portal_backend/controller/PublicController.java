package com.pravin.job_portal_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.dto.UserLoginDTO;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.service.interfaces.JobService;
import com.pravin.job_portal_backend.service.interfaces.UserProfileService;
import com.pravin.job_portal_backend.service.interfaces.UserRegistrationService;
import com.pravin.job_portal_backend.utilis.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private UserProfileService userProfileService;

  @Autowired
  private UserRegistrationService userRegistrationService;

  // @Autowired
  // private EmailService emailService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private JobService jobService;

  @GetMapping("/health-check")
  public String healthCheck() {
    log.info("Health check endpoint called");
    return "Ok";
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody UserLoginDTO userDTO) {
    log.info("Received signup request: {}", userDTO);
    Optional<UserDto> createdUser = userRegistrationService.saveUser(userDTO);

    if (createdUser.isPresent()) {
      // Send welcome email using new EmailService
      String to = createdUser.get().getEmail();
      String name = createdUser.get().getName();
      String subject = "Welcome to Job Portal!";
      String body = "Hello " + name + ",\n\nThank you for registering at Job Portal. We're excited to have you on board!\n\nBest regards,\nJob Portal Team";
      com.pravin.job_portal_backend.entity.Email email = new com.pravin.job_portal_backend.entity.Email(to, subject, body);
      // emailService.sendEmail(email); // Disabled: No SMTP configured
      return ResponseEntity.ok("User registered successfully with ID: " + createdUser.get().getId());
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signup failed");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserLoginDTO userDTO) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));

      UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
      Optional<UserDto> userOptional = userProfileService.findByUserName(userDTO.getEmail());

      if (userOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
      }

      UserDto user = userOptional.get(); // ✅ Now it's safe to call getId()
      String jwt = jwtUtil.generateToken(userDetails.getUsername(), user.getId(), userDetails.getAuthorities());

      // Notification info
      String mobileId = user.getPhoneNumber();
      String time = java.time.LocalDateTime.now().toString();
      var notification = new java.util.HashMap<String, Object>();
      notification.put("message", "Login successful");
      notification.put("time", time);
      notification.put("mobileId", mobileId);
      return ResponseEntity.ok(Map.of("token", jwt));

    } catch (Exception e) {
      log.error("Login failed: ", e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect username or password");
    }
  }

  @GetMapping("/jobs/paginated")
  public ResponseEntity<?> getAllJobsPaginated(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "postedDate") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir,
      @RequestParam(required = false) String jobTitle,
      @RequestParam(required = false) String jobLocation,
      @RequestParam(required = false) Double minSalary,
      @RequestParam(required = false) Double maxSalary) {

    try {
      List<String> allowedSortFields = List.of("postedDate", "jobSalary", "jobTitle", "jobLocation", "id");
      if (!allowedSortFields.contains(sortBy)) {
        sortBy = "postedDate";
      }

      Page<Job> pageResult = jobService.getAllJobsOfPagable(page, size, sortBy, sortDir, jobTitle, jobLocation,
          minSalary, maxSalary);

      Map<String, Object> response = new HashMap<>();
      response.put("content", pageResult.getContent());
      response.put("totalPages", pageResult.getTotalPages());
      response.put("totalElements", pageResult.getTotalElements());
      response.put("pageNumber", pageResult.getNumber());
      response.put("pageSize", pageResult.getSize());

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("Error fetching paginated jobs: ", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch jobs.");
    }
  }
}
