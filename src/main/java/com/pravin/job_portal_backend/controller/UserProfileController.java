package com.pravin.job_portal_backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.pravin.job_portal_backend.dto.UpdateUserProfile;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.service.interfaces.UserProfileService;

@RestController
@RequestMapping("/role-profile")
public class UserProfileController {

  private final UserProfileService userProfileService;

  @Autowired
  public UserProfileController(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  @PutMapping("/update-profile")
  public ResponseEntity<?> updateProfile(
      Authentication authentication,
      @RequestBody UpdateUserProfile request) {

    String username = authentication.getName();
    Optional<UserDto> currentUserOptional = userProfileService.findByUserName(username);

    if (currentUserOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    UserDto currentUser = currentUserOptional.get();
    boolean isAdmin = currentUser.getRole() != null &&
        currentUser.getRole().equalsIgnoreCase("ADMIN");

    UserDto targetUser = currentUser;

    // Admins can update other users' profiles if email is provided
    if (isAdmin && request.getEmail() != null && !request.getEmail().equalsIgnoreCase(currentUser.getEmail())) {
      Optional<UserDto> userByEmailOptional = userProfileService.findByUserName(request.getEmail());
      if (userByEmailOptional.isPresent()) {
        targetUser = userByEmailOptional.get();
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Target user not found.");
      }
    }

    // Non-admins can only update their own profile
    if (!isAdmin && !targetUser.getEmail().equalsIgnoreCase(currentUser.getEmail())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own profile.");
    }

    userProfileService.updateUserProfile(targetUser.getId(), request);
    return ResponseEntity.ok("Profile updated successfully.");
  }

  @GetMapping("/get-profile")
  public ResponseEntity<?> getProfile(Authentication authentication) {
    String username = authentication.getName();
    Optional<UserDto> userOptional = userProfileService.findByUserName(username);

    return userOptional
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."));
  }

  @GetMapping("/users-name")
  public ResponseEntity<?> getUserFullname(Authentication authentication) {
    String username = authentication.getName();
    Optional<UserDto> userOptional = userProfileService.findByUserName(username);

    if (userOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    String fullname = userOptional.get().getName();
    return ResponseEntity.ok(fullname != null ? fullname : "User");
  }
}
