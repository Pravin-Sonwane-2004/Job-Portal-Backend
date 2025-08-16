package com.pravin.job_portal_backend.service.admin;

import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.exception.EmailCredentialsNotException;
import com.pravin.job_portal_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImp {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImp.class);
    private final UserRepository repository;

    /**
     * Get user details by ID
     */
    public User getUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new EmailCredentialsNotException("User not found with ID: " + userId));
    }

    /**
     * Get user details by Email
     */
    public User getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new EmailCredentialsNotException("User not found with email: " + email));
    }

    /**
     * Update user role (promote/demote)
     */
    @Transactional
    public User updateUserRole(Long userId, Role newRole) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new EmailCredentialsNotException("User not found with ID: " + userId));

        user.setRole(newRole);
        repository.save(user);
        logger.info("Role of user {} updated to {}", user.getEmail(), newRole);

        return user;
    }

    /**
     * Block / Unblock user
     */

    @Transactional
    public User toggleBlockUser(Long userId, boolean block) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new EmailCredentialsNotException("User not found with ID: " + userId));

        user.setBlocked(block); // assumes `boolean blocked` field in User entity
        repository.save(user);

        logger.info("User {} has been {}", user.getEmail(), block ? "BLOCKED" : "UNBLOCKED");

        return user;
    }
}
