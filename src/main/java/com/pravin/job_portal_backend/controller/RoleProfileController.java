//package com.pravin.job_portal_backend.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/role-profile")
//public class RoleProfileController {
//    @GetMapping("/full-name")
//    public ResponseEntity<String> getFullName(Authentication authentication) {
//        // You can fetch the full name from your user service if needed
//        String username = authentication.getName();
//        // For demonstration, just return the username (email)
//        return ResponseEntity.ok(username);
//    }
//}
