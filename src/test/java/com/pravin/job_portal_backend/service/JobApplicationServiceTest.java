// package com.pravin.job_portal_backend.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;

// import com.pravin.job_portal_backend.dto.JobApplicationDTO;
// import com.pravin.job_portal_backend.entity.*;
// import com.pravin.job_portal_backend.repository.JobApplicationRepository;
// import com.pravin.job_portal_backend.repository.JOB_SEEKERRepository;

// class JobApplicationServiceTest {

// @Mock
// private JobApplicationRepository jobApplicationRepository;
// @Mock
// private JobApplicationRepository jobRepository;
// @Mock
// private JOB_SEEKERRepository JOB_SEEKERRepository;

// @InjectMocks
// private com.pravin.job_portal_backend.service.JobApplicationServiceImpl
// jobApplicationService;

// @BeforeEach
// void setUp() {
// MockitoAnnotations.openMocks(this);
// // Mock SecurityContext
// Authentication authentication = mock(Authentication.class);
// when(authentication.getName()).thenReturn("JOB_SEEKER");
// SecurityContext securityContext = mock(SecurityContext.class);
// when(securityContext.getAuthentication()).thenReturn(authentication);
// SecurityContextHolder.setContext(securityContext);
// }

// @Test
// void testApplyForJob() {
// JOB_SEEKER JOB_SEEKER = new JOB_SEEKER();
// JOB_SEEKER.setId(1L);
// Job job = new Job();
// job.setId(2L);

// when(JOB_SEEKERRepository.findByUSER("JOB_SEEKER")).thenReturn(Optional.of(JOB_SEEKER));
// when(jobRepository.findById(2L)).thenReturn(Optional.of(job));

// jobApplicationService.applyForJob(2L);

// verify(jobApplicationRepository, times(1)).save(any(JobApplication.class));
// }

// @Test
// void testGetMyApplications() {
// JOB_SEEKER JOB_SEEKER = new JOB_SEEKER();
// JOB_SEEKER.setId(1L);
// Job job = new Job();
// job.setId(2L);
// job.setTitle("Test Job");
// JobApplication app = new JobApplication();
// app.setId(3L);
// app.setJob(job);
// app.setJobSeeker(JOB_SEEKER);
// app.setStatus("APPLIED");
// app.setAppliedAt(LocalDateTime.now());

// when(JOB_SEEKERRepository.findByUSER("JOB_SEEKER")).thenReturn(Optional.of(JOB_SEEKER));
// when(jobApplicationRepository.findByJobSeeker(JOB_SEEKER)).thenReturn(List.of(app));

// List<JobApplicationDTO> dtos = jobApplicationService.getMyApplications();
// assertEquals(1, dtos.size());
// assertEquals("Test Job", dtos.get(0).getJobTitle());
// }

// @Test
// void testGetApplicantsForJob() {
// Job job = new Job();
// job.setId(2L);
// job.setTitle("Test Job");
// JOB_SEEKER JOB_SEEKER = new JOB_SEEKER();
// JOB_SEEKER.setId(1L);
// JobApplication app = new JobApplication();
// app.setId(3L);
// app.setJob(job);
// app.setJobSeeker(JOB_SEEKER);
// app.setStatus("APPLIED");
// app.setAppliedAt(LocalDateTime.now());

// when(jobRepository.findById(2L)).thenReturn(Optional.of(job));
// when(jobApplicationRepository.findByJob(job)).thenReturn(List.of(app));

// List<JobApplicationDTO> dtos = jobApplicationService.getApplicantsForJob(2L);
// assertEquals(1, dtos.size());
// assertEquals("Test Job", dtos.get(0).getJobTitle());
// }
// }
// }
