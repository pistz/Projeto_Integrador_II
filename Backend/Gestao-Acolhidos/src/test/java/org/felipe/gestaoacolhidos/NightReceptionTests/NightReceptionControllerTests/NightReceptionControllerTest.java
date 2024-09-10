package org.felipe.gestaoacolhidos.NightReceptionTests.NightReceptionControllerTests;

import org.felipe.gestaoacolhidos.model.controller.ExceptionHandler.ControllerExceptionHandler;
import org.felipe.gestaoacolhidos.model.controller.NightReception.NightReceptionController;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResumedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionDTO;
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
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        this.mockMvc = MockMvcBuilders.standaloneSetup(nightReceptionController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();;
    }

    @Test
    void testCreateEvent_success() throws Exception {
        // Arrange
        UUID hostedId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2024, 9, 10);

        // Monta o JSON manualmente
        String jsonContent = "{ \"date\": \"" + date + "\", \"hostedList\": [\"" + hostedId + "\"] }";

        NightReceptionResponseDTO responseDTO = new NightReceptionResponseDTO("Evento criado com sucesso!");

        when(nightReceptionService.createEvent(any(NightReceptionCreateDTO.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/night-reception/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Evento criado com sucesso!"));
    }

    @Test
    void testCreateEvent_emptyData() throws Exception {
        // Arrange
        String jsonContent = "{}"; // Simula um DTO vazio

        when(nightReceptionService.createEvent(any(NightReceptionCreateDTO.class)))
                .thenThrow(new NoSuchElementException("Dados inexistentes"));

        // Act & Assert
        mockMvc.perform(post("/night-reception/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Dados inexistentes"));
    }

    @Test
    void testCreateEvent_exceedsCapacity() throws Exception {
        // Arrange
        UUID hostedId1 = UUID.randomUUID();
        UUID hostedId2 = UUID.randomUUID();
        LocalDate date = LocalDate.of(2024, 9, 10);

        // Monta o JSON manualmente
        String jsonContent = "{ \"date\": \"" + date + "\", \"hostedList\": [\"" + hostedId1 + "\", \"" + hostedId2 + "\"] }";

        when(nightReceptionService.createEvent(any(NightReceptionCreateDTO.class)))
                .thenThrow(new IllegalArgumentException("A quantidade de acolhidos é maior do que a capacidade do albergue"));

        // Act & Assert
        mockMvc.perform(post("/night-reception/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("A quantidade de acolhidos é maior do que a capacidade do albergue"));
    }

    @Test
    void testCreateEvent_duplicateDate() throws Exception {
        // Arrange
        UUID hostedId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2024, 9, 10);

        // Monta o JSON manualmente
        String jsonContent = "{ \"date\": \"" + date + "\", \"hostedList\": [\"" + hostedId + "\"] }";

        when(nightReceptionService.createEvent(any(NightReceptionCreateDTO.class)))
                .thenThrow(new IllegalArgumentException("Já foi criado um evento para esta data"));

        // Act & Assert
        mockMvc.perform(post("/night-reception/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Já foi criado um evento para esta data"));
    }

    @Test
    void testDeleteEvent_success() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();
        NightReceptionResponseDTO responseDTO = new NightReceptionResponseDTO("Evento removido com sucesso");

        when(nightReceptionService.deleteEvent(eventId)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(delete("/night-reception/delete/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Evento removido com sucesso"));
    }

    @Test
    void testDeleteEvent_notFound() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();

        when(nightReceptionService.deleteEvent(eventId)).thenThrow(new NoSuchElementException("Não há um evento com este Id"));

        // Act & Assert
        mockMvc.perform(delete("/night-reception/delete/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não há um evento com este Id"));
    }

    @Test
    void testDeleteEvent_nullId() throws Exception {
        // Arrange
        when(nightReceptionService.deleteEvent(null)).thenThrow(new NoSuchElementException("Dados inexistentes"));

        // Act & Assert
        mockMvc.perform(delete("/night-reception/delete/{id}", (UUID) null)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateEvent_success() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();
        String jsonContent = "{\"date\": \"2024-09-15\"}"; // Exemplo de dados válidos
        NightReceptionResponseDTO responseDTO = new NightReceptionResponseDTO("Evento atualizado com sucesso!");

        when(nightReceptionService.updateEvent(eq(eventId), any(NightReceptionCreateDTO.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/night-reception/update/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Evento atualizado com sucesso!"));
    }

    @Test
    void testUpdateEvent_notFound() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();
        String jsonContent = "{\"date\": \"2024-09-15\"}";

        when(nightReceptionService.updateEvent(eq(eventId), any(NightReceptionCreateDTO.class)))
                .thenThrow(new NoSuchElementException("Não há um evento com este Id"));

        // Act & Assert
        mockMvc.perform(put("/night-reception/update/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não há um evento com este Id"));
    }

    @Test
    void testUpdateEvent_emptyData() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();
        String jsonContent = "{}"; // Dados vazios

        when(nightReceptionService.updateEvent(eq(eventId), any(NightReceptionCreateDTO.class)))
                .thenThrow(new NoSuchElementException("Não há dados na requisição"));

        // Act & Assert
        mockMvc.perform(put("/night-reception/update/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não há dados na requisição"));
    }

    @Test
    void testUpdateEvent_capacityExceeded() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();
        String jsonContent = "{\"date\": \"2024-09-15\", \"hosteds\": [1, 2, 3, 4, 5]}"; // Simulação de capacidade excedida

        when(nightReceptionService.updateEvent(eq(eventId), any(NightReceptionCreateDTO.class)))
                .thenThrow(new IllegalArgumentException("Capacidade do albergue é menor do que a quantidade de nomes enviados"));

        // Act & Assert
        mockMvc.perform(put("/night-reception/update/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Capacidade do albergue é menor do que a quantidade de nomes enviados"));
    }

    @Test
    void testUpdateEvent_duplicateDate() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();
        String jsonContent = "{\"date\": \"2024-09-15\"}";

        when(nightReceptionService.updateEvent(eq(eventId), any(NightReceptionCreateDTO.class)))
                .thenThrow(new IllegalArgumentException("Já foi criado um evento nesta data"));

        // Act & Assert
        mockMvc.perform(put("/night-reception/update/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Já foi criado um evento nesta data"));
    }

    @Test
    void testGetAll_success() throws Exception {
        // Arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<HostedResumedDTO> hostedList1 = List.of(new HostedResumedDTO(UUID.randomUUID(), "Details 1", "Test", "123.123.321-32"));
        List<HostedResumedDTO> hostedList2 = List.of(new HostedResumedDTO(UUID.randomUUID(), "Details 2", "Tester", "333.333.333-55"));

        List<NightReceptionDTO> events = List.of(
                new NightReceptionDTO(id1, LocalDate.of(2024, 9, 15), hostedList1),
                new NightReceptionDTO(id2, LocalDate.of(2024, 9, 16), hostedList2)
        );

        when(nightReceptionService.findAll()).thenReturn(events);

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_emptyList() throws Exception {
        // Arrange
        List<NightReceptionDTO> emptyList = List.of();

        when(nightReceptionService.findAll()).thenReturn(emptyList);

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")); // Verifica se o corpo da resposta é uma lista vazia
    }

    @Test
    void getEventById_success() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();
        List<HostedResumedDTO> hostedList = List.of(
                new HostedResumedDTO(UUID.randomUUID(), "Details", "Name", "123.123.321-32")
        );

        NightReceptionDTO dto = new NightReceptionDTO(
                eventId,
                LocalDate.of(2024, 9, 15),
                hostedList
        );

        when(nightReceptionService.findById(eventId)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getEventById_notFound() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();

        when(nightReceptionService.findById(eventId)).thenThrow(new NoSuchElementException("Não há um evento com este Id"));

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não há um evento com este Id"));
    }

    @Test
    void getEventById_internalServerError() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();

        when(nightReceptionService.findById(eventId)).thenThrow(new RuntimeException("SOMETHING WENT WRONG"));

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("SOMETHING WENT WRONG"));
    }

    @Test
    void getEventByDate_success() throws Exception {
        // Arrange
        LocalDate eventDate = LocalDate.of(2024, 9, 15);
        UUID eventId = UUID.randomUUID();
        List<HostedResumedDTO> hostedList = List.of(
                new HostedResumedDTO(UUID.randomUUID(), "Details", "Name", "123.123.321-32")
        );

        NightReceptionDTO dto = new NightReceptionDTO(
                eventId,
                eventDate,
                hostedList
        );

        when(nightReceptionService.findByEventDate(eventDate)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/date")
                        .param("date", eventDate.toString()) // Adiciona o parâmetro da data
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getEventByDate_notFound() throws Exception {
        // Arrange
        LocalDate eventDate = LocalDate.of(2024, 9, 15);

        when(nightReceptionService.findByEventDate(eventDate))
                .thenThrow(new NoSuchElementException("Não há um evento nesta data"));

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/date")
                        .param("date", eventDate.toString()) // Adiciona o parâmetro da data
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não há um evento nesta data"));
    }

    @Test
    void getAllEventsByMonthAndYear_success() throws Exception {
        // Arrange
        int month = 9;
        int year = 2024;
        UUID eventId1 = UUID.randomUUID();
        UUID eventId2 = UUID.randomUUID();

        List<HostedResumedDTO> hostedList1 = List.of(
                new HostedResumedDTO(UUID.randomUUID(), "Details 1", "Name 1", "123.123.321-32")
        );
        List<HostedResumedDTO> hostedList2 = List.of(
                new HostedResumedDTO(UUID.randomUUID(), "Details 2", "Name 2", "333.333.333-55")
        );

        List<NightReceptionDTO> events = List.of(
                new NightReceptionDTO(eventId1, LocalDate.of(year, month, 15), hostedList1),
                new NightReceptionDTO(eventId2, LocalDate.of(year, month, 16), hostedList2)
        );


        when(nightReceptionService.findAllEventsByMonthAndYear(month, year)).thenReturn(events);

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/month-year")
                        .param("month", String.valueOf(month))
                        .param("year", String.valueOf(year))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEventsByMonthAndYear_notFound() throws Exception {
        // Arrange
        int month = 9;
        int year = 2024;

        when(nightReceptionService.findAllEventsByMonthAndYear(month, year))
                .thenThrow(new NoSuchElementException("Não há eventos neste mês"));

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/month-year")
                        .param("month", String.valueOf(month))
                        .param("year", String.valueOf(year))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não há eventos neste mês"));
    }

    @Test
    void getAllEventsByYear_success() throws Exception {
        // Arrange
        int year = 2024;
        UUID eventId1 = UUID.randomUUID();
        UUID eventId2 = UUID.randomUUID();

        List<HostedResumedDTO> hostedList1 = List.of(
                new HostedResumedDTO(UUID.randomUUID(), "Details 1", "Name 1", "123.123.321-32")
        );
        List<HostedResumedDTO> hostedList2 = List.of(
                new HostedResumedDTO(UUID.randomUUID(), "Details 2", "Name 2", "333.333.333-55")
        );

        List<NightReceptionDTO> events = List.of(
                new NightReceptionDTO(eventId1, LocalDate.of(year, 9, 15), hostedList1),
                new NightReceptionDTO(eventId2, LocalDate.of(year, 10, 16), hostedList2)
        );

        when(nightReceptionService.findAllEventsByYear(year)).thenReturn(events);

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/year")
                        .param("year", String.valueOf(year))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEventsByYear_notFound() throws Exception {
        // Arrange
        int year = 2024;

        when(nightReceptionService.findAllEventsByYear(year))
                .thenThrow(new NoSuchElementException("Não há eventos neste ano"));

        // Act & Assert
        mockMvc.perform(get("/night-reception/find/year")
                        .param("year", String.valueOf(year))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não há eventos neste ano"));
    }

}
