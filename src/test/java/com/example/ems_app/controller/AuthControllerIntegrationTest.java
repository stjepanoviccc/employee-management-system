package com.example.ems_app.controller;

import com.example.ems_app.config.SecurityConfiguration;
import com.example.ems_app.dto.LoginRequestDTO;
import com.example.ems_app.dto.LoginResponseDTO;
import com.example.ems_app.dto.UserDTO;
import com.example.ems_app.service.impl.AuthServiceImpl;
import com.example.ems_app.service.impl.JwtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserDTO userDTO = new UserDTO(1L, "username", "password", "ROLE_ADMIN");;
    private final String baseUrl = "/api/v1/auth";

    @MockBean
    private AuthServiceImpl authService;

    @MockBean
    private JwtServiceImpl jwtService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Test
    public void testLogin() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("username", "password");;
        LoginResponseDTO mockResponse = new LoginResponseDTO("token123");

        when(authService.login(loginRequestDTO)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value(mockResponse.getAccessToken()));
    }

    @Test
    public void testRegister() throws Exception {
        when(authService.register(userDTO)).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userDTO.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(userDTO.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").value(userDTO.getRoles()));
    }
}