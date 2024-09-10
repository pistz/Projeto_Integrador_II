package org.felipe.gestaoacolhidos.CapacityTests.CapacityControllerTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.felipe.gestaoacolhidos.model.controller.Capacity.CapacityController;
import org.felipe.gestaoacolhidos.model.domain.service.Capacity.CapacityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CapacityControllerTest {

    @Mock
    private CapacityService capacityService;

    @InjectMocks
    private CapacityController capacityController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(capacityController).build();
    }

    @Test
    void testGetMaxCapacity() throws Exception {
        // Arrange
        int expectedCapacity = 100;
        when(capacityService.getCapacity()).thenReturn(expectedCapacity);

        // Act & Assert
        mockMvc.perform(get("/capacity/get"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedCapacity)));
    }

    @Test
    void testUpdateCurrentCapacity() throws Exception {
        // Arrange
        int newCapacity = 150;

        // Act & Assert
        mockMvc.perform(put("/capacity/update")
                        .contentType("application/json")
                        .content(String.valueOf(newCapacity)))
                .andExpect(status().isCreated())
                .andExpect(content().string("new capacity: " + newCapacity));

        // Verify
        verify(capacityService, times(1)).updateCapacity(newCapacity);
    }

}