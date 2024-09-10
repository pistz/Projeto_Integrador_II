package org.felipe.gestaoacolhidos.CapacityTests.CapacityServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.felipe.gestaoacolhidos.model.domain.entity.Capacity.Capacity;
import org.felipe.gestaoacolhidos.model.domain.service.Capacity.CapacityServiceImpl;
import org.felipe.gestaoacolhidos.model.repository.capacity.CapacityReposity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CapacityServiceImplTests {

    @Mock
    private CapacityReposity capacityRepository;

    @InjectMocks
    private CapacityServiceImpl capacityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCapacity_Success() {
        // Arrange
        int expectedCapacity = 100;
        when(capacityRepository.findCurrentMaxCapacity()).thenReturn(Optional.of(expectedCapacity));

        // Act
        int actualCapacity = capacityService.getCapacity();

        // Assert
        assertEquals(expectedCapacity, actualCapacity);
    }

    @Test
    void testGetCapacity_NoValuePresent() {
        // Arrange
        when(capacityRepository.findCurrentMaxCapacity()).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> capacityService.getCapacity());
    }

    @Test
    void testGetCapacity_RepositoryThrowsException() {
        // Arrange
        when(capacityRepository.findCurrentMaxCapacity()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> capacityService.getCapacity());
        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void testUpdateCapacity_CreateNew() {
        // Arrange
        int newCapacity = 150;
        when(capacityRepository.findCurrentMaxCapacity()).thenReturn(Optional.empty());

        // Act
        capacityService.updateCapacity(newCapacity);

        // Assert
        verify(capacityRepository, times(1)).save(argThat(capacity ->
                capacity.getMaxCapacity() == newCapacity &&
                        capacity.getId() != null &&
                        capacity.getUpdatedAt() != null
        ));
    }

    @Test
    void testUpdateCapacity_UpdateExisting() {
        // Arrange
        int updatedCapacity = 200;
        UUID id = UUID.randomUUID();
        Capacity existingCapacity = new Capacity(id, 100, LocalDate.now().minusDays(1));
        when(capacityRepository.findCurrentConfig()).thenReturn(Optional.of(existingCapacity));

        // Act
        capacityService.updateCapacity(updatedCapacity);

        // Assert
        verify(capacityRepository, times(1)).save(argThat(capacity ->
                capacity.getId().equals(id) &&
                        capacity.getMaxCapacity() == updatedCapacity &&
                        !capacity.getUpdatedAt().isBefore(LocalDate.now().minusDays(1))
        ));
    }

}
