package org.felipe.gestaoacolhidos.AuthTests.AuthServiceTests;

import org.felipe.gestaoacolhidos.model.domain.entity.User.User;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.felipe.gestaoacolhidos.model.repository.user.UserRepository;
import org.felipe.gestaoacolhidos.model.security.JwtUtil;
import org.felipe.gestaoacolhidos.model.service.Auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_Success() {
        String email = "user@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        String token = "jwtToken";
        User user = new User(); // Initialize with appropriate constructor or setters
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRoles(Role.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(email, user.getRoles())).thenReturn(token);

        String result = authService.authenticateUser(email, password);

        assertEquals(token, result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(jwtUtil, times(1)).generateToken(email, user.getRoles());
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        String email = "user@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.authenticateUser(email, password));
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtUtil, never()).generateToken(any(), any());
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        String email = "user@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        User user = new User(); // Initialize with appropriate constructor or setters
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRoles(Role.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.authenticateUser(email, password));
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(jwtUtil, never()).generateToken(any(), any());
    }
}
