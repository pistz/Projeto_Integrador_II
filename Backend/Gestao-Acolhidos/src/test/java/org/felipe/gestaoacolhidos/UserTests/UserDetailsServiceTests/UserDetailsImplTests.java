package org.felipe.gestaoacolhidos.UserTests.UserDetailsServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.felipe.gestaoacolhidos.model.domain.entity.User.User;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.felipe.gestaoacolhidos.model.domain.service.User.UserDetails.UserDetailsServiceImpl;
import org.felipe.gestaoacolhidos.model.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        String email = "user@example.com";
        String encodedPassword = "encodedPassword";
        org.felipe.gestaoacolhidos.model.domain.entity.User.User user = new User(); // Initialize with appropriate constructor or setters
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRoles(Role.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertEquals(email, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth ->
                    auth.getAuthority().equals("ADMIN")
                ));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String email = "nonexistentuser@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });

        assertEquals("User not found with email: " + email, thrown.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
