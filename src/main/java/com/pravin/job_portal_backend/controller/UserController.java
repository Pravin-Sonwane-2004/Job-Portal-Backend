//package com.pravin.job_portal_backend.controller;
//
//import com.pravin.job_portal_backend.dto.user_dtos.UserDetialsDto;
//import com.pravin.job_portal_backend.service.interfaces.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//    @Autowired
//    private UserService userService;
//
//    @GetMapping
//    public ResponseEntity<List<UserDetialsDto>> getAllUsers() {
//        List<UserDetialsDto> users = userService.getAllUsers();
//        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<UserDetialsDto> getUserById(@PathVariable Long id) {
//        UserDetialsDto user = userService.getUserById(id);
//        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok().build();
//    }
//}
