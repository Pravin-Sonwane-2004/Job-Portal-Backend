//package com.pravin.job_portal_backend.controller;
//
//import com.pravin.job_portal_backend.dto.user_dtos.UserLoginDTO;
//import com.pravin.job_portal_backend.dto.user_dtos.UserDetialsDto;
//import com.pravin.job_portal_backend.service.authService.UserAuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/register")
//public class UserRegistrationController {
//    @Autowired
//    private UserAuthService userAuthService;
//
//    @PostMapping
//    public ResponseEntity<UserDetialsDto> registerUser(@RequestBody UserLoginDTO userLoginDTO) {
//        Optional<UserDetialsDto> user = userAuthService.saveUser(userLoginDTO);
//        return user.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.badRequest().build());
//    }
//
//    @GetMapping("/by-email")
//    public ResponseEntity<UserDetialsDto> getUserByEmail(@RequestParam String email) {
//        Optional<UserDetialsDto> user = userAuthService.findByEmail(email);
//        return user.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//}
