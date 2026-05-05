package com.pravin.job_portal_backend.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Optional;

@Service("userDetailsService")
public class CustomUserDetailsServiceImpl implements com.pravin.job_portal_backend.service.interfaces.CustomUserDetailsService {

    // This service is the DB bridge for Spring Security login.
    // DaoAuthenticationProvider calls loadUserByUsername(email), then compares
    // the returned encoded password with the raw password from the login request.

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // The method name says "username" because it comes from Spring Security,
        // but this project uses email as the username/login identifier.
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            logger.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        User user = userOptional.get();
        logger.info("Loading user: {}", user.getEmail());

        // SimpleGrantedAuthority is Spring Security's basic role/permission type.
        // hasRole("USER") checks for the authority string "ROLE_USER", so we add
        // the ROLE_ prefix before storing it in UserDetails.
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        logger.debug("User authorities Role : {}", authorities);

        // org.springframework.security.core.userdetails.User is Spring's built-in
        // UserDetails implementation. It is not your entity class; it is a
        // security object containing login identity, encoded password, and roles.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
