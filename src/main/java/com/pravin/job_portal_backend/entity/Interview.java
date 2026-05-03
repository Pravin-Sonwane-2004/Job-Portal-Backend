package com.pravin.job_portal_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interviews")
public class Interview {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "application_id", nullable = false)
  private ApplyJob application;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "employer_id", nullable = false)
  private User employer;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "candidate_id", nullable = false)
  private User candidate;

  @Column(name = "scheduled_time", nullable = false)
  private LocalDateTime scheduledTime;

  @Column(nullable = false, length = 40)
  private String status;

  @Column(length = 512)
  private String meetingLink;

  @Column(length = 1000)
  private String notes;
}
