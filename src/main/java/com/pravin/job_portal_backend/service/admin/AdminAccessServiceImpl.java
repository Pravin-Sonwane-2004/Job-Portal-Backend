package com.pravin.job_portal_backend.service.admin;

import com.pravin.job_portal_backend.dto.user_dtos.UserDetialsDto;
import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.exception.UnauthorizedRoleAssignmentException;
import com.pravin.job_portal_backend.mapper.user_mapper.UserAuthMapper;
import com.pravin.job_portal_backend.mapper.user_mapper.UserDetialsMapper;
import com.pravin.job_portal_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminAccessServiceImpl {

}
