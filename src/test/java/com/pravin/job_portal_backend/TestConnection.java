package com.pravin.job_portal_backend;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
  public static void main(String[] args) {
    // Define connection parameters
    String url = "jdbc:postgresql://springbootdb.cit44a4s0ha4.us-east-1.rds.amazonaws.com:5432/springappdb";
    String username = "pravin";
    String password = "Admin12345";

    try {
      // Attempt to establish connection
      Connection conn = DriverManager.getConnection(url, username, password);
      System.out.println("✅ Connected successfully to AWS RDS PostgreSQL.");
      conn.close();
    } catch (Exception e) {
      System.out.println("❌ Connection failed.");
      e.printStackTrace();
    }
  }
}
