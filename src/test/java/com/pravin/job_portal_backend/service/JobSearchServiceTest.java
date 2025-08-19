// package com.pravin.job_portal_backend.service;

// import com.pravin.job_portal_backend.entity.Job;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;
// import org.springframework.data.domain.*;

// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class JobSearchServiceTest {

//     @Mock
//     private JobApplyService jobSearchService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testSearchJobs() {
//         Page<Job> page = new PageImpl<>(List.of(new Job()));
//         when(jobSearchService.searchJobs(any(), any(), any(), any(), any(Pageable.class))).thenReturn(page);

//         Page<Job> result = jobSearchService.searchJobs("IT", "NY", 1000, "Java", PageRequest.of(0, 10));
//         assertEquals(1, result.getTotalElements());
//     }
// }
