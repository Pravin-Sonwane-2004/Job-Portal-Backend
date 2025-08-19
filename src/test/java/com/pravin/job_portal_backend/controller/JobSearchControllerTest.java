// package com.pravin.job_portal_backend.controller;

// import com.pravin.job_portal_backend.controller.job.JobApplyController;
// import com.pravin.job_portal_backend.entity.Job;
// import com.pravin.job_portal_backend.service.job_service.JobApplyService;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import java.util.List;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// class JobSearchControllerTest {

//     @Mock
//     private JobApplyService jobSearchService;

//     @InjectMocks
//     private JobApplyController jobSearchController;

//     private MockMvc mockMvc;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(jobSearchController).build();
//     }

//     @Test
//     void testSearchJobs() throws Exception {
//         Page<Job> page = new PageImpl<>(List.of(new Job()));
//         when(jobSearchService.searchJobs(any(), any(), any(), any(), any(PageRequest.class))).thenReturn(page);

//         mockMvc.perform(get("/api/jobs/search"))
//                 .andExpect(status().isOk());
//     }
// }
