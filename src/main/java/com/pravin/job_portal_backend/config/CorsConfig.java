package com.pravin.job_portal_backend.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Value("${app.frontend-urls:}")
    private String frontendUrls;

    @Bean
    //Run this component before almost everything else.
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(allowedOriginPatterns());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/admin/all")
                        .allowedOriginPatterns(allowedOriginPatterns().toArray(String[]::new))
                        .allowedMethods("GET")
                        .allowedHeaders("*")
                        .allowCredentials(true);
                registry.addMapping("/**")
                        .allowedOriginPatterns(allowedOriginPatterns().toArray(String[]::new))
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    private List<String> allowedOriginPatterns() {
        List<String> origins = new ArrayList<>();
        origins.add("http://localhost:*");
        origins.add("http://127.0.0.1:*");
        addOrigin(origins, frontendUrl);
        for (String origin : frontendUrls.split(",")) {
            addOrigin(origins, origin);
        }
        return origins;
    }

    private void addOrigin(List<String> origins, String origin) {
        String trimmedOrigin = origin == null ? "" : origin.trim();
        if (!trimmedOrigin.isEmpty() && !origins.contains(trimmedOrigin)) {
            origins.add(trimmedOrigin);
        }
    }
}
