package com.pravin.job_portal_backend.controller.admin;


import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "pravin@gmail.com";
            String adminName = "pravin";
            String adminPassword = "admin123"; // You can move this to application.properties for security

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setName(adminName);
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
                System.out.println("✅ Default admin created: " + adminEmail);
            } else {
                System.out.println("ℹ️ Admin already exists: " + adminEmail);
            }
        };
    }
}
