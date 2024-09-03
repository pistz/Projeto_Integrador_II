package org.felipe.gestaoacolhidos.AuthTests.AuthControllerTests;
import org.felipe.gestaoacolhidos.model.controller.Auth.AuthController;
import org.felipe.gestaoacolhidos.model.controller.ExceptionHandler.ControllerExceptionHandler;
import org.felipe.gestaoacolhidos.model.dto.User.UserLoginDTO;
import org.felipe.gestaoacolhidos.model.service.Auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void testLogin_Success() throws Exception {
        UserLoginDTO loginDTO = new UserLoginDTO("user@example.com", "password123");
        String token = "jwtToken";

        when(authService.authenticateUser(loginDTO.email(), loginDTO.password())).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(token));

        verify(authService, times(1)).authenticateUser(loginDTO.email(), loginDTO.password());
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        UserLoginDTO loginDTO = new UserLoginDTO("user@example.com", "password123");

        when(authService.authenticateUser(loginDTO.email(), loginDTO.password()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isNotFound());

        verify(authService, times(1)).authenticateUser(loginDTO.email(), loginDTO.password());
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        UserLoginDTO loginDTO = new UserLoginDTO("user@example.com", "password123");

        when(authService.authenticateUser(loginDTO.email(), loginDTO.password()))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isBadRequest());

        verify(authService, times(1)).authenticateUser(loginDTO.email(), loginDTO.password());
    }
}