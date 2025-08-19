// package com.pravin.job_portal_backend.controller.profile;

// import java.util.Optional;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// import
// com.pravin.job_portal_backend.dto.JOB_SEEKER_dtos.JOB_SEEKERDetialsDto;
// import
// com.pravin.job_portal_backend.service.profile.JOB_SEEKERProfileService;

// @RestController
// @RequestMapping("/role-profile")
// public class JOB_SEEKERProfileController {

// private final JOB_SEEKERProfileService JOB_SEEKERProfileService;

// public JOB_SEEKERProfileController(JOB_SEEKERProfileService
// JOB_SEEKERProfileService) {
// this.JOB_SEEKERProfileService = JOB_SEEKERProfileService;
// }

// @PutMapping("/update-profile")
// public ResponseEntity<?> updateProfile(Authentication authentication,
// @RequestBody UpdateJOB_SEEKERProfile request) {

// String USER = authentication.getName();
// Optional<JOB_SEEKERDetialsDto> currentJOB_SEEKEROptional =
// JOB_SEEKERProfileService.findByUSER(USER);

// if (currentJOB_SEEKEROptional.isEmpty()) {
// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
// }

// JOB_SEEKERDetialsDto currentJOB_SEEKER = currentJOB_SEEKEROptional.get();
// boolean isAdmin = currentJOB_SEEKER.getRole() != null &&
// currentJOB_SEEKER.getRole().equalsIgnoreCase("ADMIN");

// JOB_SEEKERDetialsDto targetJOB_SEEKER = currentJOB_SEEKER;

// // Admins can update other JOB_SEEKERs' profiles if email is provided
// if (isAdmin && request.getEmail() != null &&
// !request.getEmail().equalsIgnoreCase(currentJOB_SEEKER.getEmail())) {
// Optional<JOB_SEEKERDetialsDto> JOB_SEEKERByEmailOptional =
// JOB_SEEKERProfileService.findByUSER(request.getEmail());
// if (JOB_SEEKERByEmailOptional.isPresent()) {
// targetJOB_SEEKER = JOB_SEEKERByEmailOptional.get();
// } else {
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Target JOB_SEEKER
// not found.");
// }
// }

// // Non-admins can only update their own profile
// if (!isAdmin &&
// !targetJOB_SEEKER.getEmail().equalsIgnoreCase(currentJOB_SEEKER.getEmail()))
// {
// return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update
// your own profile.");
// }

// JOB_SEEKERProfileService.updateJOB_SEEKERProfile(targetJOB_SEEKER.getId(),
// request);
// return ResponseEntity.ok("Profile updated successfully.");
// }

// @GetMapping("/get-profile")
// public ResponseEntity<?> getProfile(Authentication authentication) {
// String USER = authentication.getName();
// Optional<JOB_SEEKERDetialsDto> JOB_SEEKEROptional =
// JOB_SEEKERProfileService.findByUSER(USER);

// return JOB_SEEKEROptional
// .<ResponseEntity<?>>map(ResponseEntity::ok)
// .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("JOB_SEEKER
// not found."));
// }

// @GetMapping("/JOB_SEEKERs-name")
// public ResponseEntity<?> getJOB_SEEKERFullname(Authentication authentication)
// {
// String USER = authentication.getName();
// Optional<JOB_SEEKERDetialsDto> JOB_SEEKEROptional =
// JOB_SEEKERProfileService.findByUSER(USER);

// if (JOB_SEEKEROptional.isEmpty()) {
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JOB_SEEKER not
// found.");
// }

// String fullname = JOB_SEEKEROptional.get().getName();
// return ResponseEntity.ok(fullname != null ? fullname : "JOB_SEEKER");
// }
// }
