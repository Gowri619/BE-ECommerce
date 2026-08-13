package com.gowri.commerceflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gowri.commerceflow.dto.request.LoginRequest;
import com.gowri.commerceflow.dto.request.RegisterRequest;
import com.gowri.commerceflow.dto.response.LoginResponse;
import com.gowri.commerceflow.dto.response.RegisterResponse;
import com.gowri.commerceflow.service.AuthService;
import com.gowri.commerceflow.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void register_returnsCreatedUser() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Gowri");
        request.setEmail("gowri@example.com");
        request.setPassword("password123");

        RegisterResponse response = RegisterResponse.builder()
                .id(1L)
                .name("Gowri")
                .email("gowri@example.com")
                .role("USER")
                .build();

        when(userService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("gowri@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void login_returnsJwtToken() throws Exception {
        LoginRequest request = new LoginRequest("gowri@example.com", "password123");
        LoginResponse response = new LoginResponse("test-jwt-token");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }
}
