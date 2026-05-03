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

  private final JwtFilter jwtFilter;
  private final UserDetailsService userDetailsService;
  private final Environment environment;

  public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService, Environment environment) {
    this.jwtFilter = jwtFilter;
    this.userDetailsService = userDetailsService;
    this.environment = environment;
  }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(withDefaults())
        .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

            // ✅ Browser preflight requests must pass before auth checks
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            // ✅ Public: Swagger only for non-prod
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

            // ✅ Public endpoints
            .requestMatchers("/admin/first-admin-signup").permitAll()
            .requestMatchers("/user/jobs/paginated").permitAll()
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/api/register/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/companies/**").permitAll()

            // ✅ Admin endpoints
            .requestMatchers("admin/admin/all-appliers").hasRole("ADMIN")
            .requestMatchers("/admin/users").hasRole("ADMIN")
            .requestMatchers("/admin/user/{username}").hasRole("ADMIN")
            .requestMatchers("/admin/jobs/**").hasRole("ADMIN")
            .requestMatchers("/admin/applications/**").hasRole("ADMIN")
            .requestMatchers("/admin/**").hasRole("ADMIN")

            // ✅ Recruiter endpoints
            .requestMatchers("/recruiter/**").hasRole("RECRUITER")

            // ✅ User endpoints
            .requestMatchers("/user/jobs").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/user/jobs/{id}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/user/jobs/sorted").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/user/profile").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/user/**").hasRole("USER")

            // ✅ Job applications
            .requestMatchers("/apply/applications/apply").hasRole("USER")
            .requestMatchers("/apply/applications/my-applied-dto").hasRole("USER")
            .requestMatchers("/apply/applications/my-applied-entities").hasRole("USER")
            .requestMatchers("/apply/applications/cancel").hasRole("USER")
            .requestMatchers("/apply/applications/admin/all").hasRole("ADMIN")
            .requestMatchers("/apply/applications/**").hasRole("USER")

            // ✅ Saved Jobs
            .requestMatchers("/api/saved-jobs/user/{userId}").hasRole("USER")
            .requestMatchers("/api/saved-jobs/save").hasRole("USER")
            .requestMatchers("/api/saved-jobs/unsave").hasRole("USER")
            .requestMatchers("/api/saved-jobs/**").hasRole("USER")

            // ✅ User Management
            .requestMatchers("/api/users/{id}").hasRole("ADMIN")
            .requestMatchers("/api/users/**").hasRole("ADMIN")

            // ✅ Email
            .requestMatchers("/email/send").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/email/**").hasAnyRole("USER", "ADMIN")

            // ✅ Role Profile (all endpoints)
            .requestMatchers("/role-profile/update-profile").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/role-profile/get-profile").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/role-profile/users-name").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/role-profile/full-name").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/role-profile/**").hasAnyRole("USER", "ADMIN")

            // ✅ Job Alerts
            .requestMatchers("/api/job-alerts/user/{userId}").hasRole("USER")
            .requestMatchers("/api/job-alerts/create").hasRole("USER")
            .requestMatchers("/api/job-alerts/delete/{alertId}").hasRole("USER")
            .requestMatchers("/api/job-alerts/**").hasRole("USER")

            // ✅ Resumes
            .requestMatchers("/api/resumes/user/{userId}").hasRole("USER")
            .requestMatchers("/api/resumes/upload").hasRole("USER")
            .requestMatchers("/api/resumes/delete/{resumeId}").hasRole("USER")
            .requestMatchers("/api/resumes/**").hasRole("USER")

            // ✅ Messages
            .requestMatchers("/api/messages/sent/{userId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/messages/received/{userId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/messages/conversation").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/messages/send").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/messages/delete/{messageId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/messages/**").hasAnyRole("USER", "ADMIN")

            // ✅ Companies (view: all, create/update/delete: admin)
            .requestMatchers(HttpMethod.GET, "/api/companies/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/companies/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/companies/**").hasRole("ADMIN")
            .requestMatchers("/api/companies/**").hasRole("ADMIN")

            // ✅ Company Reviews
            .requestMatchers("/api/company-reviews/company/{companyId}").hasRole("USER")
            .requestMatchers("/api/company-reviews/add").hasRole("USER")
            .requestMatchers("/api/company-reviews/delete/{reviewId}").hasRole("USER")
            .requestMatchers("/api/company-reviews/**").hasRole("USER")

            // ✅ Interviews
            .requestMatchers("/api/interviews/user/{userId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/interviews/employer/{employerId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/interviews/application/{applicationId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/interviews/schedule").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/interviews/update-status/{interviewId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/interviews/delete/{interviewId}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/interviews/**").hasAnyRole("USER", "ADMIN")

            // ✅ All other requests require authentication
            .anyRequest().authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
    return (WebSecurity web) -> web.ignoring()
        .requestMatchers(HttpMethod.OPTIONS, "/**")
        .requestMatchers("/public/**", "/api/register/**");
  }

    @Bean
    AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

    @Bean
    PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
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
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("Authorization"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
