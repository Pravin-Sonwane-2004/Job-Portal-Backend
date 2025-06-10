package com.pravin.job_portal_backend.exception;

public class UnauthorizedRoleAssignmentException extends RuntimeException {
  public UnauthorizedRoleAssignmentException(String message) {
    super(message);
  }
}
