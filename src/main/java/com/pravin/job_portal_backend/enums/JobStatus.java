package com.pravin.job_portal_backend.enums;

/**
 * Lifecycle state of a job posting.
 *
 * OPEN: visible and accepting applications.
 * CLOSED: no longer accepting applications.
 * DRAFT: saved by recruiter/admin but not published yet.
 */
public enum JobStatus {
  OPEN,
  CLOSED,
  DRAFT
}
