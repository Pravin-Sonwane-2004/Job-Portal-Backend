package com.pravin.job_portal_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**") // apply to all endpoints
            .allowedOrigins("*") // allow all origins
            .allowedMethods("*") // allow all HTTP methods
            .allowedHeaders("*") // allow all headers
            .allowCredentials(false) // disable credentials for wildcard origins
            .maxAge(3600); // cache pre-flight response for 1 hour
      }
    };
  }
}
