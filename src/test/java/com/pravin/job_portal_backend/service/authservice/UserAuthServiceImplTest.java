package com.pravin.job_portal_backend.service.authservice;


import com.pravin.job_portal_backend.dto.user_dtos.UserLoginDTO;
import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.mapper.user_mapper.UserAuthMapper;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.authService.UserAuthServiceImpl;
import com.pravin.job_portal_backend.service.email.EmailService;
import com.pravin.job_portal_backend.utilis.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    private UserRegistrationDTO registrationDto;
    private UserLoginDTO loginDto;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDTO();
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");
        registrationDto.setRole(Role.USER);
        registrationDto.setName("Test User");

        loginDto = new UserLoginDTO();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password123");
    }

    // ---------- REGISTER TESTS ----------

    @Test
    void register_shouldSaveUser_whenValid() {
        when(repository.findByEmail(registrationDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User savedUser = UserAuthMapper.toRegistrationEntity(registrationDto);
        savedUser.setId(1L);
        savedUser.setPassword("encodedPassword");

        when(repository.saveAndFlush(any(User.class))).thenReturn(savedUser);

        var result = userAuthService.register(registrationDto);

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(repository).saveAndFlush(any(User.class));
        verify(emailService).sendRegistrationWelcomeEmail("test@example.com");
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        when(repository.findByEmail(registrationDto.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class,
                () -> userAuthService.register(registrationDto));

        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void register_shouldThrowException_whenAdminRoleAttempted() {
        registrationDto.setRole(Role.ADMIN);

        assertThrows(IllegalArgumentException.class,
                () -> userAuthService.register(registrationDto));

        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void register_shouldNotFail_whenEmailSendingFails() {
        when(repository.findByEmail(registrationDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User savedUser = UserAuthMapper.toRegistrationEntity(registrationDto);
        savedUser.setId(1L);
        savedUser.setPassword("encodedPassword");

        when(repository.saveAndFlush(any(User.class))).thenReturn(savedUser);
        doThrow(new RuntimeException("SMTP error"))
                .when(emailService).sendRegistrationWelcomeEmail(anyString());

        var result = userAuthService.register(registrationDto);

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    // ---------- LOGIN TESTS ----------

    @Test
    void loginAndGenerateToken_shouldReturnToken_whenValidCredentials() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        when(repository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(eq("test@example.com"), eq(1L), any()))
                .thenReturn("jwt-token");

        String token = userAuthService.loginAndGenerateToken(loginDto);

        assertEquals("jwt-token", token);
        verify(jwtUtil).generateToken(eq("test@example.com"), eq(1L), any());
    }

    @Test
    void loginAndGenerateToken_shouldThrowException_whenUserNotFound() {
        when(repository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> userAuthService.loginAndGenerateToken(loginDto));
    }

    @Test
    void loginAndGenerateToken_shouldThrowException_whenPasswordMismatch() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(repository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userAuthService.loginAndGenerateToken(loginDto));
    }
}
