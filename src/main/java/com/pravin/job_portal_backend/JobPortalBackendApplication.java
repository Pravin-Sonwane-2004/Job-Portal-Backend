package com.pravin.job_portal_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class JobPortalBackendApplication {

  public static void main(String[] args) {
    // Log key environment variables for debugging
    System.out.println("DB_URL: " + System.getenv("DB_URL"));
    System.out.println("DB_USERNAME: " + System.getenv("DB_USERNAME"));
    System.out.println("MAIL_USERNAME: " + System.getenv("MAIL_USERNAME"));
    System.out.println("PORT: " + System.getenv("PORT"));
    SpringApplication.run(JobPortalBackendApplication.class, args);
  }

}
