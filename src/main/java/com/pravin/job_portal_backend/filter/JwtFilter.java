package com.pravin.job_portal_backend.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pravin.job_portal_backend.utilis.JwtUtil;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

  // OncePerRequestFilter comes from Spring Web. It guarantees this filter runs
  // once per request, even if the request is internally forwarded.

  // UserDetailsService is a Spring Security interface for loading user data.
  // This project has a bean named "userDetailsService" in
  // CustomUserDetailsServiceImpl. The current filter trusts roles from JWT, so
  // this field is available if you later want to reload/check the user from DB.
  @Autowired
  @Qualifier("userDetailsService")
  private UserDetailsService userDetailsService;

  // JwtUtil is our own helper class. It knows how to read, create, and validate
  // JWT tokens using the configured secret key.
  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    // JWT flow for protected requests:
    // 1. Client sends: Authorization: Bearer <token>
    // 2. This filter extracts and validates the token.
    // 3. If valid, it creates Authentication and stores it in SecurityContext.
    // 4. Spring Security then checks endpoint rules like hasRole("USER").
    String authorizationHeader = request.getHeader( "Authorization");
    String username = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      // Remove "Bearer " prefix. The rest is the compact JWT string.
      jwt = authorizationHeader.substring(7);
      try {
        // Subject is the username/email we stored when generating the token.
        username = jwtUtil.extractUsername(jwt);
      } catch (JwtException | IllegalArgumentException ex) {
        // Invalid/expired/malformed tokens should not crash the request.
        // We simply leave the request unauthenticated; security rules later
        // return 401/403 if the endpoint requires authentication.
        logger.debug("Ignoring invalid JWT: " + ex.getMessage());
      }
    }

    if (username != null) {
      if (jwtUtil.validateToken(jwt)) {
        // Roles were stored in the JWT as strings like ROLE_USER or ROLE_ADMIN.
        // SimpleGrantedAuthority is Spring Security's simple authority object.
        java.util.List<String> roles = jwtUtil.extractRoles(jwt);
        java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities =
            roles.stream().map(org.springframework.security.core.authority.SimpleGrantedAuthority::new).toList();

        // UsernamePasswordAuthenticationToken is Spring Security's Authentication
        // implementation. Here it means: "this request is authenticated as this
        // username with these roles". Credentials are null because password login
        // already happened earlier; this request uses JWT instead.
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

        // Adds request details such as remote address/session id for auditing.
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // SecurityContextHolder stores Authentication for the current thread.
        // Controllers and @PreAuthorize read the logged-in user from here.
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }
    // Continue to the next filter, then eventually the controller.
    chain.doFilter(request, response);
  }
}
