package com.pravin.job_portal_backend.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// UserDetailsService is a Spring Security interface.
// Spring calls loadUserByUsername(...) during username/password authentication.
// We extend it so our service can use email as the login username and still be
// accepted anywhere Spring expects a UserDetailsService bean.
public interface CustomUserDetailsService extends UserDetailsService {
    // UserDetails is Spring Security's standard user object. It contains the
    // username, encoded password, and authorities/roles used for authorization.
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
