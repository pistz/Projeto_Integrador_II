package org.felipe.gestaoacolhidos.UserTests.UserServiceTests;

import org.felipe.gestaoacolhidos.model.domain.entity.User.User;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserDeletedDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserResponseDTO;
import org.felipe.gestaoacolhidos.model.exceptions.UserAlreadyExistsException;
import org.felipe.gestaoacolhidos.model.repository.user.UserRepository;
import org.felipe.gestaoacolhidos.model.service.User.UserService;
import org.felipe.gestaoacolhidos.model.service.User.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("test@example.com", "password123", "ADMIN");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        UserCreatedResponseDTO response = userService.createUser(userCreateDTO);

        assertNotNull(response);
        assertTrue(response.message().contains("User created:"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_InvalidEmail() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("invalidEmail", "password123", "ADMIN");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userCreateDTO);
        });

        assertEquals("E-mail inválido", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("test@example.com", "password123", "ADMIN");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(userCreateDTO);
        });

        assertEquals("Usuario já existe, não pode ser registrado novamente", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(true);

        UserDeletedDTO response = userService.deleteUser(userId);

        assertNotNull(response);
        assertEquals("User deleted: " + userId, response.message());
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userService.deleteUser(userId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void testDeleteUser_NullUserId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(null);
        });

        assertEquals("No user selected", exception.getMessage());
        verify(userRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void testFindAll_Success() {
        List<User> users = List.of(
                new User(UUID.randomUUID(), "user1@example.com", "encodedPassword", Role.ADMIN),
                new User(UUID.randomUUID(), "user2@example.com", "encodedPassword", Role.SECRETARY)
        );
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).email());
        assertEquals(Role.ADMIN, result.get(0).role());
        assertEquals("user2@example.com", result.get(1).email());
        assertEquals(Role.SECRETARY, result.get(1).role());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "user@example.com","encodedPassword", Role.ADMIN);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.findById(userId);

        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("user@example.com", response.email());
        assertEquals(Role.ADMIN, response.role());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userService.findById(userId);
        });

        assertEquals("Not found user with id: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }
}