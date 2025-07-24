package com.pravin.job_portal_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.enums.ExperienceLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AdminController adminUserService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldReturnListOfUsers_whenUsersExist() throws Exception {
    // Arrange
    UserDto user = UserDto.builder()
        .id(1L)
        .email("pravin@gmail.com")
        .name("Pravin")
        .avatarUrl("http://avatar.com/img.jpg")
        .designation("Developer")
        .verified(true)
        .location("Mumbai")
        .bio("Backend Developer")
        .phoneNumber("9999999999")
        .linkedinUrl("https://linkedin.com/in/pravin")
        .jobRole("Java Developer")
        .role("USER")
        .experienceLevel(ExperienceLevel.JUNIOR)
        .roles(List.of("ROLE_USER"))
        .skills(List.of("Java", "Spring Boot"))
        .build();

    Mockito.when(adminUserService.getAllUsers()).thenReturn((ResponseEntity<List<UserDto>>) List.of(user));

    // Act & Assert
    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].email").value("pravin@gmail.com"))
        .andExpect(jsonPath("$[0].name").value("Pravin"))
        .andExpect(jsonPath("$[0].designation").value("Developer"))
        .andExpect(jsonPath("$[0].verified").value(true))
        .andExpect(jsonPath("$[0].location").value("Mumbai"))
        .andExpect(jsonPath("$[0].bio").value("Backend Developer"))
        .andExpect(jsonPath("$[0].phoneNumber").value("9999999999"))
        .andExpect(jsonPath("$[0].linkedinUrl").value("https://linkedin.com/in/pravin"))
        .andExpect(jsonPath("$[0].jobRole").value("Java Developer"))
        .andExpect(jsonPath("$[0].role").value("USER"))
        .andExpect(jsonPath("$[0].experienceLevel").value("JUNIOR"))
        .andExpect(jsonPath("$[0].roles[0]").value("ROLE_USER"))
        .andExpect(jsonPath("$[0].skills[0]").value("Java"))
        .andExpect(jsonPath("$[0].skills[1]").value("Spring Boot"));
  }

  @Test
  void shouldReturnNoContent_whenUserListIsEmpty() throws Exception {
    Mockito.when(adminUserService.getAllUsers()).thenReturn((ResponseEntity<List<UserDto>>) List.of());

    mockMvc.perform(get("/users"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNoContent_whenUserListIsNull() throws Exception {
    Mockito.when(adminUserService.getAllUsers()).thenReturn(null);

    mockMvc.perform(get("/users"))
        .andExpect(status().isNoContent());
  }
}
