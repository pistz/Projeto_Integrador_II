package org.felipe.gestaoacolhidos.NightReceptionTests.NightReceptionControllerTests;

import org.felipe.gestaoacolhidos.model.controller.NightReception.NightReceptionController;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionResponseDTO;
import org.felipe.gestaoacolhidos.model.domain.service.NightReception.NightReceptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NightReceptionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NightReceptionService nightReceptionService;

    @InjectMocks
    private NightReceptionController nightReceptionController;


    @BeforeEach
    void setUp() {
        // Inicializa os mocks e configura o MockMvc manualmente
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(nightReceptionController).build();
    }

    @Test
    void createEvent_success() throws Exception {
        // Arrange
        NightReceptionCreateDTO dto = new NightReceptionCreateDTO(
                LocalDate.of(2024, 9, 10),
                List.of(UUID.randomUUID()) // Lista de IDs de acolhidos
        );
        UUID hostedId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2024, 9, 10);
        String jsonContent = "{ \"date\": \"" + date + "\", \"hostedList\": [\"" + hostedId + "\"] }";

        NightReceptionResponseDTO responseDTO = new NightReceptionResponseDTO("Evento criado com sucesso!");

        when(nightReceptionService.createEvent(any(NightReceptionCreateDTO.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Evento criado com sucesso!"));
    }

}
