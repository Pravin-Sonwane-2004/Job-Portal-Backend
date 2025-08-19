package com.pravin.job_portal_backend.service.admin;

import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.exception.EmailCredentialsNotException;
import com.pravin.job_portal_backend.exception.UnauthorizedRoleAssignmentException;
import com.pravin.job_portal_backend.mapper.user_mapper.UserAuthMapper;
import com.pravin.job_portal_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EmailCredentialsNotException("User not found with ID: " + userId));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailCredentialsNotException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public User updateUserRole(Long userId, Role newRole) {
        User user = getUserById(userId);
        user.setRole(newRole);
        userRepository.save(user);
        logger.info("Role of {} updated to {}", user.getEmail(), newRole);
        return user;
    }

    @Override
    @Transactional
    public User toggleBlockUser(Long userId, boolean block) {
        User user = getUserById(userId);
        user.setBlocked(block);
        userRepository.save(user);
        logger.info("User {} has been {}", user.getEmail(), block ? "BLOCKED" : "UNBLOCKED");
        return user;
    }

    @Override
    public List<UserDetails> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only admin can access all users.");
        }
        return userRepository.findAll().stream()
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .disabled(user.isBlocked())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByUsername(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            logger.info("User {} deleted by admin", email);
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

    @Override
    @Transactional
    public Optional<UserRegistrationDTO> registerAdmin(UserRegistrationDTO registrationDto) {
        User user = UserAuthMapper.toRegistrationEntity(registrationDto);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UnauthorizedRoleAssignmentException("Email already registered.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);

        User savedAdmin = userRepository.saveAndFlush(user);

        return Optional.of(UserAuthMapper.toRegistrationDto(savedAdmin));
    }
}

