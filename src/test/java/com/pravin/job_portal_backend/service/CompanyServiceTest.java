// package com.pravin.job_portal_backend.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;

// import com.pravin.job_portal_backend.dto.company_dtos.CompanyDTO;
// import com.pravin.job_portal_backend.entity.Company;
// import com.pravin.job_portal_backend.exception.ResourceNotFoundException;
// import com.pravin.job_portal_backend.repository.CompanyRepository;

// class CompanyServiceTest {

//     @Mock
//     private CompanyRepository companyRepository;

//     @InjectMocks
//     private com.pravin.job_portal_backend.service.company.CompanyServiceImpl companyService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testGetCompanyById() {
//         Company company = new Company();
//         company.setId(1L);
//         company.setName("Test");
//         when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

//         CompanyDTO dto = companyService.getCompanyById(1L);
//         assertEquals("Test", dto.getName());
//     }

//     @Test
//     void testGetCompanyByIdNotFound() {
//         when(companyRepository.findById(1L)).thenReturn(Optional.empty());
//         assertThrows(ResourceNotFoundException.class, () -> companyService.getCompanyById(1L));
//     }

//     @Test
//     void testGetAllCompanies() {
//         Company company = new Company();
//         company.setId(1L);
//         company.setName("Test");
//         when(companyRepository.findAll()).thenReturn(List.of(company));

//         List<CompanyDTO> dtos = companyService.getAllCompanies(null);
//         assertEquals(1, dtos.size());
//         assertEquals("Test", dtos.get(0).getName());
//     }

//     @Test
//     void testCreateCompany() {
//         CompanyDTO dto = new CompanyDTO();
//         dto.setName("Test");
//         Company company = new Company();
//         company.setId(1L);
//         company.setName("Test");
//         when(companyRepository.save(any(Company.class))).thenReturn(company);

//         CompanyDTO result = companyService.createCompany(dto);
//         assertEquals("Test", result.getName());
//     }

//     @Test
//     void testUpdateCompany() {
//         Company company = new Company();
//         company.setId(1L);
//         company.setName("Old");
//         CompanyDTO dto = new CompanyDTO();
//         dto.setName("New");
//         when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
//         when(companyRepository.save(any(Company.class))).thenReturn(company);

//         CompanyDTO result = companyService.updateCompany(1L, dto);
//         assertEquals("New", result.getName());
//     }

//     @Test
//     void testDeleteCompany() {
//         Company company = new Company();
//         company.setId(1L);
//         when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
//         doNothing().when(companyRepository).delete(company);

//         assertDoesNotThrow(() -> companyService.deleteCompany(1L));
//         verify(companyRepository, times(1)).delete(company);
//     }
// }
