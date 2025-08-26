package com.pravin.job_portal_backend.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.pravin.job_portal_backend.filter.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // enables @PreAuthorize with hierarchy
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;
    private final Environment environment;

    public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService, Environment environment) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
        this.environment = environment;
    }

    // ✅ Role hierarchy: ADMIN > RECRUITER > USER
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_RECRUITER \n ROLE_RECRUITER > ROLE_USER");
        return hierarchy;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Swagger (only enabled for non-prod)
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
                        .requestMatchers("/auth/**", "/public/**", "/api/auth/register/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/companies/**").permitAll()

                        // ✅ Admin endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/companies/**").hasRole("ADMIN")

                        // ✅ Recruiter endpoints
                        .requestMatchers("/recruiter/**").hasRole("RECRUITER")

                        // ✅ User endpoints
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/apply/**").hasRole("USER")
                        .requestMatchers("/api/saved-jobs/**").hasRole("USER")
                        .requestMatchers("/api/job-alerts/**").hasRole("USER")
                        .requestMatchers("/api/resumes/**").hasRole("USER")
                        .requestMatchers("/api/company-reviews/**").hasRole("USER")

                        // ✅ Shared (USER + ADMIN automatically handled by hierarchy)
                        .requestMatchers("/email/**").hasRole("USER")
                        .requestMatchers("/role-profile/**").hasRole("USER")
                        .requestMatchers("/api/messages/**").hasRole("USER")
                        .requestMatchers("/api/interviews/**").hasRole("USER")

                        // ✅ Any other request requires authentication
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
