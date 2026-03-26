package com.pravin.job_portal_backend.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

            // ✅ Public: Swagger only for non-prod
            .requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**").permitAll()

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
            .requestMatchers("/user/jobs").hasRole("USER")
            .requestMatchers("/user/jobs/{id}").hasRole("USER")
            .requestMatchers("/user/jobs/sorted").hasRole("USER")
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

    // ✅ Use environment variable for prod URL, fallback to localhost
    String frontendUrl = Optional.ofNullable(System.getenv("FRONTEND_URL"))
        .orElse("http://localhost:3000");

    configuration.setAllowedOrigins(List.of(frontendUrl));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
