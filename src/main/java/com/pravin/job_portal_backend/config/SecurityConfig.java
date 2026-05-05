package com.pravin.job_portal_backend.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.core.env.Environment;

import com.pravin.job_portal_backend.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  // JwtFilter is our custom servlet filter. It reads the Bearer token from the
  // Authorization header and puts Authentication into Spring SecurityContext.
  private final JwtFilter jwtFilter;

  // UserDetailsService is a Spring Security interface. DaoAuthenticationProvider
  // calls it during login to load the user record by username/email from DB.
  private final UserDetailsService userDetailsService;

  // Environment is a Spring Core object used here to check active profiles,
  // for example to disable Swagger access when the "prod" profile is active.
  private final Environment environment;

  public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService, Environment environment) {
    this.jwtFilter = jwtFilter;
    this.userDetailsService = userDetailsService;
    this.environment = environment;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // SecurityFilterChain is the main Spring Security configuration bean.
    // Every HTTP request passes through this chain before it reaches a controller.
    http
        // Enables CORS using the CorsConfigurationSource bean below.
        .cors(withDefaults())
        // CSRF protection is usually for browser session/cookie apps. This API
        // uses stateless JWT tokens, so CSRF is disabled.
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth

            // Browser preflight requests must pass before auth checks.
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            // Swagger is public only outside production. "access" lets us make
            // a custom AuthorizationDecision instead of using only role checks.
            .requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**")
            .access((authentication, context) -> {
              boolean isProd = environment.acceptsProfiles(Profiles.of("prod"));
              return new org.springframework.security.authorization.AuthorizationDecision(!isProd);
            })

            // Public endpoints do not need a JWT token.
            .requestMatchers("/admin/first-admin-signup").permitAll()
            .requestMatchers("/user/jobs/paginated").permitAll()
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/api/register/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/companies/**").permitAll()

            // hasRole("X") checks for an authority named "ROLE_X".
            // Example: hasRole("COMPANY_ADMIN") requires ROLE_COMPANY_ADMIN.
            .requestMatchers("/company-portal/employees/**").hasRole("COMPANY_ADMIN")
            .requestMatchers("/company-portal/**").hasAnyRole("COMPANY_ADMIN", "COMPANY_EMPLOYEE")

            // Admin endpoints need ROLE_ADMIN.
            .requestMatchers("admin/admin/all-appliers").hasRole("ADMIN")
            .requestMatchers("/admin/users").hasRole("ADMIN")
            .requestMatchers("/admin/user/{username}").hasRole("ADMIN")
            .requestMatchers("/admin/jobs/**").hasRole("ADMIN")
            .requestMatchers("/admin/applications/**").hasRole("ADMIN")
            .requestMatchers("/admin/**").hasRole("ADMIN")

            // Recruiter endpoints need ROLE_RECRUITER.
            .requestMatchers("/recruiter/**").hasRole("RECRUITER")

            // User endpoints mostly need ROLE_USER. Some read endpoints also
            // allow ROLE_ADMIN so admins can view user-side resources.
            .requestMatchers("/user/jobs").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/user/jobs/{id}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/user/jobs/sorted").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/user/profile").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/user/**").hasRole("USER")

            // Job application permissions.
            .requestMatchers("/apply/applications/apply").hasRole("USER")
            .requestMatchers("/apply/applications/my-applied-dto").hasRole("USER")
            .requestMatchers("/apply/applications/my-applied-entities").hasRole("USER")
            .requestMatchers("/apply/applications/cancel").hasRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/apply/applications/{applicationId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/apply/applications/{applicationId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/apply/applications/admin/all").hasRole("ADMIN")
            .requestMatchers("/apply/applications/**").hasRole("USER")

            // Saved job APIs are user-only because saved jobs belong to a user.
            .requestMatchers("/api/saved-jobs/user/{userId}").hasRole("USER")
            .requestMatchers("/api/saved-jobs/save").hasRole("USER")
            .requestMatchers("/api/saved-jobs/unsave").hasRole("USER")
            .requestMatchers("/api/saved-jobs/**").hasRole("USER")

            // User management APIs are admin-only.
            .requestMatchers("/api/users/{id}").hasRole("ADMIN")
            .requestMatchers("/api/users/**").hasRole("ADMIN")

            // Role profile insight APIs are shared by authenticated user roles.
            .requestMatchers("/api/profile-insights/**").hasAnyRole("USER", "ADMIN", "RECRUITER")

            // Email APIs are allowed for all authenticated application roles.
            .requestMatchers("/email/send").hasAnyRole("USER", "ADMIN", "RECRUITER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE")
            .requestMatchers("/email/**").hasAnyRole("USER", "ADMIN", "RECRUITER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE")

            // Role profile APIs are allowed for all authenticated application roles.
            .requestMatchers("/role-profile/update-profile").hasAnyRole("USER", "ADMIN", "RECRUITER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE")
            .requestMatchers("/role-profile/get-profile").hasAnyRole("USER", "ADMIN", "RECRUITER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE")
            .requestMatchers("/role-profile/users-name").hasAnyRole("USER", "ADMIN", "RECRUITER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE")
            .requestMatchers("/role-profile/full-name").hasAnyRole("USER", "ADMIN", "RECRUITER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE")
            .requestMatchers("/role-profile/**").hasAnyRole("USER", "ADMIN", "RECRUITER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE")

            // Job alert APIs are user-only.
            .requestMatchers("/api/job-alerts/user/{userId}").hasRole("USER")
            .requestMatchers("/api/job-alerts/create").hasRole("USER")
            .requestMatchers("/api/job-alerts/delete/{alertId}").hasRole("USER")
            .requestMatchers("/api/job-alerts/**").hasRole("USER")

            // Resume APIs are user-only.
            .requestMatchers("/api/resumes/user/{userId}").hasRole("USER")
            .requestMatchers("/api/resumes/upload").hasRole("USER")
            .requestMatchers("/api/resumes/delete/{resumeId}").hasRole("USER")
            .requestMatchers("/api/resumes/**").hasRole("USER")

            // Message APIs are allowed for all authenticated application roles.
            .requestMatchers("/api/messages/**").hasAnyRole("USER", "ADMIN", "RECRUITER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE")

            // Company data can be viewed publicly, but write/delete actions are admin-only.
            .requestMatchers(HttpMethod.GET, "/api/companies/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/companies/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/companies/**").hasRole("ADMIN")
            .requestMatchers("/api/companies/**").hasRole("ADMIN")

            // Company reviews can be read publicly; create/update needs USER or ADMIN;
            // delete is admin-only.
            .requestMatchers(HttpMethod.GET, "/api/company-reviews/**").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/api/company-reviews/**").hasAnyRole("ADMIN")
            .requestMatchers("/api/company-reviews/**").hasAnyRole("USER", "ADMIN")

            // Interview APIs are shared by authenticated user roles.
            .requestMatchers("/api/interviews/**").hasAnyRole("USER", "ADMIN", "RECRUITER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE")

            // If no rule above matches, the request still needs a valid authenticated user.
            .anyRequest().authenticated())
        // STATELESS means Spring Security will not create/use an HTTP session.
        // Each request must prove itself again with the JWT token.
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Registers DaoAuthenticationProvider for username/password login checks.
        .authenticationProvider(authenticationProvider())
        // Places JwtFilter before Spring's UsernamePasswordAuthenticationFilter.
        // So JWT authentication is prepared before Spring checks authorization rules.
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {
    // WebSecurityCustomizer completely skips Spring Security filters for these
    // paths. Use this only for truly public/static endpoints.
    return (WebSecurity web) -> web.ignoring()
        .requestMatchers(HttpMethod.OPTIONS, "/**")
        .requestMatchers("/public/**", "/api/register/**");
  }

  @Bean
  AuthenticationProvider authenticationProvider() {
    // AuthenticationProvider is a Spring Security strategy interface.
    // DaoAuthenticationProvider is Spring's DB-backed implementation:
    // 1. call userDetailsService.loadUserByUsername(email)
    // 2. compare raw login password with encoded DB password
    // 3. return authenticated user + authorities if both are valid
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    // AuthenticationManager is the entry point used by login code.
    // In PublicController, authenticationManager.authenticate(...) delegates to
    // the AuthenticationProvider above.
    return config.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    // PasswordEncoder is a Spring Security interface. BCryptPasswordEncoder
    // hashes passwords for storage and verifies raw login passwords safely.
    return new BCryptPasswordEncoder();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    // CORS decides which frontend origins are allowed to call this backend
    // from a browser. Non-browser clients like Postman are not blocked by CORS.
    CorsConfiguration configuration = new CorsConfiguration();

    List<String> allowedOriginPatterns = new ArrayList<>(List.of(
        "http://localhost:*",
        "http://127.0.0.1:*"));

    String frontendUrls = System.getenv("FRONTEND_URLS");
    if (frontendUrls != null && !frontendUrls.isBlank()) {
      allowedOriginPatterns.addAll(Arrays.stream(frontendUrls.split(","))
          .map(String::trim)
          .filter(value -> !value.isBlank())
          .toList());
    }

    String frontendUrl = System.getenv("FRONTEND_URL");
    if (frontendUrl != null && !frontendUrl.isBlank()) {
      allowedOriginPatterns.add(frontendUrl.trim());
    }

    configuration.setAllowedOriginPatterns(allowedOriginPatterns);
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("Authorization"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
