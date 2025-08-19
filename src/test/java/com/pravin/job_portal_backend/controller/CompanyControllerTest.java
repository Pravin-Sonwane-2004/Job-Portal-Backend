// package com.pravin.job_portal_backend.controller;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import com.pravin.job_portal_backend.controller.company.CompanyController;
// import com.pravin.job_portal_backend.dto.company_dtos.CompanyDTO;
// import com.pravin.job_portal_backend.service.company.CompanyService;

// class CompanyControllerTest {

//     @Mock
//     private CompanyService companyService;

//     @InjectMocks
//     private CompanyController companyController;

//     private MockMvc mockMvc;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
//     }

//     @Test
//     void testGetCompanyById() throws Exception {
//         CompanyDTO dto = new CompanyDTO();
//         dto.setId(1L);
//         dto.setName("Test");
//         when(companyService.getCompanyById(1L)).thenReturn(dto);

//         mockMvc.perform(get("/api/companies/1"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testGetAllCompanies() throws Exception {
//         Pageable pageable = PageRequest.of(0, 10);
//         when(companyService.getAllCompanies(any(Pageable.class))).thenReturn(List.of(new CompanyDTO()));
//         mockMvc.perform(get("/api/companies"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testCreateCompany() throws Exception {
//         CompanyDTO dto = new CompanyDTO();
//         dto.setName("Test");
//         dto.setLocation("Test City");
//         dto.setIndustry("IT");
//         dto.setContactEmail("test@example.com");
//         when(companyService.createCompany(any(CompanyDTO.class))).thenReturn(dto);

//         mockMvc.perform(post("/api/companies")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(
//                         "{\"name\":\"Test\",\"location\":\"Test City\",\"industry\":\"IT\",\"contactEmail\":\"test@example.com\"}"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testUpdateCompany() throws Exception {
//         CompanyDTO dto = new CompanyDTO();
//         dto.setName("Updated");
//         dto.setLocation("Updated City");
//         dto.setIndustry("Finance");
//         dto.setContactEmail("updated@example.com");
//         when(companyService.updateCompany(eq(1L), any(CompanyDTO.class))).thenReturn(dto);

//         mockMvc.perform(put("/api/companies/1")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(
//                         "{\"name\":\"Updated\",\"location\":\"Updated City\",\"industry\":\"Finance\",\"contactEmail\":\"updated@example.com\"}"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testDeleteCompany() throws Exception {
//         doNothing().when(companyService).deleteCompany(1L);
//         mockMvc.perform(delete("/api/companies/1"))
//                 .andExpect(status().isOk());
//     }
// }
