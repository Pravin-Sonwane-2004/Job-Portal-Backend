package com.pravin.job_portal_backend.service.impl;

import com.pravin.job_portal_backend.service.interfaces.AdminUserService;
import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.mapper.UserMapper;
import com.pravin.job_portal_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements com.pravin.job_portal_backend.service.interfaces.AdminUserService {

    @Autowired
    private UserRepository repository;

    @Override
    public List<UserDto> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new org.springframework.security.access.AccessDeniedException("Only admin can access all users.");
        }
        return repository.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllUsersNoAuth() {
        return repository.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteByUsername(String email) {
        Optional<User> userOptional = repository.findByEmail(email);
        if (userOptional.isPresent()) {
            repository.delete(userOptional.get());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
    }
}
