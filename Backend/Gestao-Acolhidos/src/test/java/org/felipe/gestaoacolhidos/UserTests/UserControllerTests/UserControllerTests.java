package org.felipe.gestaoacolhidos.UserTests.UserControllerTests;

import org.felipe.gestaoacolhidos.model.controller.ExceptionHandler.ControllerExceptionHandler;
import org.felipe.gestaoacolhidos.model.controller.User.UserController;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserResponseDTO;
import org.felipe.gestaoacolhidos.model.exceptions.UserAlreadyExistsException;
import org.felipe.gestaoacolhidos.model.domain.service.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class UserControllerTests {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    @WithMockUser // Simula um usuário autenticado
    void testGetAll_Success() {
        List<UserResponseDTO> users = List.of(
                new UserResponseDTO(UUID.randomUUID(), "user1@example.com", Role.ADMIN),
                new UserResponseDTO(UUID.randomUUID(), "user2@example.com", Role.SECRETARY)
        );

        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<UserResponseDTO>> response = userController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(userService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    void testGetById_Success() {
        UUID userId = UUID.randomUUID();
        UserResponseDTO user = new UserResponseDTO(userId, "user@example.com", Role.ADMIN);

        when(userService.findById(userId)).thenReturn(user);

        ResponseEntity<UserResponseDTO> response = userController.getById(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, Objects.requireNonNull(response.getBody()).id());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    @WithMockUser
    void testGetById_UserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        when(userService.findById(userId)).thenThrow(new NoSuchElementException("Not found user with id: " + userId));

        mockMvc.perform(get("/user/find/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found user with id: " + userId));

        verify(userService, times(1)).findById(userId);
    }

    @Test
    @WithMockUser
    void testCreateUser_Success() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("newuser@example.com", "password123", "ADMIN");
        UserCreatedResponseDTO userCreatedResponseDTO = new UserCreatedResponseDTO("User created: 1");

        when(userService.createUser(userCreateDTO)).thenReturn(userCreatedResponseDTO);

        ResponseEntity<UserCreatedResponseDTO> response = userController.createUser(userCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User created: 1", Objects.requireNonNull(response.getBody()).message());
        verify(userService, times(1)).createUser(userCreateDTO);
    }

    @Test
    @WithMockUser
    void testCreateUser_UserAlreadyExists() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("existinguser@example.com", "password123", "ADMIN");

        when(userService.createUser(userCreateDTO)).thenThrow(new UserAlreadyExistsException("Usuario já existe, não pode ser registrado novamente"));

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"existinguser@example.com\", \"password\": \"password123\", \"role\": \"ADMIN\" }"))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().string("Usuario já existe, não pode ser registrado novamente"));

        verify(userService, times(1)).createUser(userCreateDTO);
    }

    @Test
    @WithMockUser
    void testCreateUser_InvalidEmail() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("invalid-email", "password123", "ADMIN");

        when(userService.createUser(userCreateDTO)).thenThrow(new IllegalArgumentException("E-mail inválido"));

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"invalid-email\", \"password\": \"password123\", \"role\": \"ADMIN\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("E-mail inválido"));

        verify(userService, times(1)).createUser(userCreateDTO);
    }

    @Test
    @WithMockUser
    void testDeleteUser_Success() {
        UUID userId = UUID.randomUUID();

        ResponseEntity response = userController.deleteUser(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @WithMockUser
    void testDeleteUser_UserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        doThrow(new NoSuchElementException("User not found")).when(userService).deleteUser(userId);

        mockMvc.perform(delete("/user/delete/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @WithMockUser
    void testDeleteUser_InvalidId() throws Exception {
        String invalidId = "invalid-uuid";

        when(userService.deleteUser(any(UUID.class))).thenThrow(new IllegalArgumentException("Invalid ID"));

        mockMvc.perform(delete("/user/delete/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).deleteUser(any(UUID.class));
    }
}
