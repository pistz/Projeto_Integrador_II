package org.felipe.gestaoacolhidos.NightReceptionTests.NightReceptionServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.NightReception.NightReceptionResponseDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.domain.entity.NightReception.NightReception;
import org.felipe.gestaoacolhidos.model.domain.service.NightReception.NightReceptionServiceImpl;
import org.felipe.gestaoacolhidos.model.logs.UserLoggingInterceptor;
import org.felipe.gestaoacolhidos.model.repository.capacity.CapacityReposity;
import org.felipe.gestaoacolhidos.model.repository.hosted.HostedRepository;
import org.felipe.gestaoacolhidos.model.repository.nightReception.NightReceptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NightReceptionServiceImplTest {

    @Mock
    private NightReceptionRepository nightReceptionRepository;

    @Mock
    private HostedRepository hostedRepository;

    @Mock
    private CapacityReposity capacityReposity;

    @Mock
    private UserLoggingInterceptor userLoggingInterceptor;

    @InjectMocks
    private NightReceptionServiceImpl nightReceptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testCreateEvent_DateRepeated() {
        // Arrange
        LocalDate eventDate = LocalDate.of(2024, 9, 15);
        NightReceptionCreateDTO dto = new NightReceptionCreateDTO(eventDate, new ArrayList<>());
        NightReception nightReception = new NightReception();
        when(nightReceptionRepository.findByEventDate(eventDate)).thenReturn(Optional.of(nightReception));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> nightReceptionService.createEvent(dto));
        assertEquals("Já foi criado um evento para esta data", thrown.getMessage());
    }

    @Test
    void testCreateEvent_InvalidRequest() {
        // Arrange
        LocalDate eventDate = LocalDate.of(2024, 9, 15);
        NightReceptionCreateDTO dto = new NightReceptionCreateDTO(eventDate, new ArrayList<>());
        when(nightReceptionRepository.findByEventDate(eventDate)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> nightReceptionService.createEvent(dto));
        assertEquals("No value present", thrown.getMessage());
    }

    @Test
    void testCreateEvent_Success() {
        // Arrange
        LocalDate eventDate = LocalDate.of(2024, 9, 15);
        NightReceptionCreateDTO dto = new NightReceptionCreateDTO(eventDate, List.of(UUID.randomUUID()));

        // Simulate no existing event
        when(nightReceptionRepository.findByEventDate(eventDate)).thenReturn(Optional.empty());

        // Simulate valid capacity
        when(capacityReposity.findCurrentMaxCapacity()).thenReturn(Optional.of(10));

        // Simulate hosted entities
        UUID hostedId = dto.hostedList().get(0);
        Hosted hosted = Hosted.builder().build();
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));

        // Simulate user logging interceptor
        when(userLoggingInterceptor.getRegisteredUser()).thenReturn("user123");

        // Act
        NightReceptionResponseDTO response = nightReceptionService.createEvent(dto);

        // Assert
        assertEquals("Evento criado com sucesso!", response.message());
    }

    @Test
    void testUpdateEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        LocalDate eventDate = LocalDate.of(2024, 9, 15);
        NightReceptionCreateDTO dto = new NightReceptionCreateDTO(eventDate, List.of(UUID.randomUUID()));

        NightReception existingEvent = new NightReception();
        existingEvent.setId(eventId);
        existingEvent.setEventDate(LocalDate.of(2024, 9, 10));
        existingEvent.setHosteds(new ArrayList<>());

        when(nightReceptionRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(nightReceptionRepository.findByEventDate(eventDate)).thenReturn(Optional.empty());
        when(capacityReposity.findCurrentMaxCapacity()).thenReturn(Optional.of(10));
        when(hostedRepository.findById(any(UUID.class))).thenReturn(Optional.of(Hosted.builder().build()));
        when(userLoggingInterceptor.getRegisteredUser()).thenReturn("user123");

        // Act
        NightReceptionResponseDTO response = nightReceptionService.updateEvent(eventId, dto);

        // Assert
        assertEquals("Evento atualizado com sucesso!", response.message());
    }

    @Test
    void testUpdateEvent_EventNotFound() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        NightReceptionCreateDTO dto = new NightReceptionCreateDTO(LocalDate.now(), new ArrayList<>());

        // Simulate event not found
        when(nightReceptionRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> nightReceptionService.updateEvent(eventId, dto));
        assertEquals("Evento inexistente", thrown.getMessage());
    }

    @Test
    void testUpdateEvent_DateRepeatedForDifferentEvent() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        LocalDate eventDate = LocalDate.of(2024, 9, 15);
        NightReceptionCreateDTO dto = new NightReceptionCreateDTO(eventDate, List.of(UUID.randomUUID()));

        NightReception existingEvent = new NightReception();
        existingEvent.setId(eventId);
        existingEvent.setEventDate(LocalDate.of(2024, 9, 10));
        existingEvent.setHosteds(new ArrayList<>());

        NightReception anotherEvent = new NightReception();
        anotherEvent.setId(UUID.randomUUID());
        anotherEvent.setEventDate(eventDate);

        when(nightReceptionRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(nightReceptionRepository.findByEventDate(eventDate)).thenReturn(Optional.of(anotherEvent));
        when(capacityReposity.findCurrentMaxCapacity()).thenReturn(Optional.of(10));
        when(hostedRepository.findById(any(UUID.class))).thenReturn(Optional.of(Hosted.builder().build()));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> nightReceptionService.updateEvent(eventId, dto));
        assertEquals("Já foi criado um evento para esta data", thrown.getMessage());
    }

    @Test
    void testUpdateEvent_InvalidRequestDateNotFound() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        LocalDate eventDate = LocalDate.of(2024, 9, 15);
        NightReceptionCreateDTO dto = new NightReceptionCreateDTO(eventDate, new ArrayList<>());

        NightReception existingEvent = new NightReception();
        existingEvent.setId(eventId);
        existingEvent.setEventDate(LocalDate.of(2024, 9, 10));
        existingEvent.setHosteds(new ArrayList<>());

        when(nightReceptionRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(nightReceptionRepository.findByEventDate(eventDate)).thenReturn(Optional.empty());
        when(hostedRepository.findById(any(UUID.class))).thenReturn(Optional.of(Hosted.builder().build()));

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> nightReceptionService.updateEvent(eventId, dto));
        assertEquals("No value present", thrown.getMessage());
    }

    @Test
    void delete_success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        NightReception nightReception = new NightReception();
        nightReception.setId(eventId);

        when(nightReceptionRepository.findById(eventId)).thenReturn(Optional.of(nightReception));

        // Act
        NightReceptionResponseDTO response = nightReceptionService.deleteEvent(eventId);

        // Assert
        verify(nightReceptionRepository, times(1)).delete(nightReception);
        assertEquals("Evento removido com sucesso!", response.message());
    }

    @Test
    void delete_nullEventId() {
        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> nightReceptionService.deleteEvent(null));

        assertEquals("Dados inexistentes", thrown.getMessage());
        verify(nightReceptionRepository, never()).delete(any());
    }

    @Test
    void delete_eventNotFound() {
        // Arrange
        UUID eventId = UUID.randomUUID();

        when(nightReceptionRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> nightReceptionService.deleteEvent(eventId));

        assertNotNull(thrown);
        verify(nightReceptionRepository, never()).delete(any());
    }

    @Test
    void testFindByMonthAndYear_success() {
        // Arrange
        int month = 9;
        int year = 2024;

        NightReception event1 = new NightReception();
        event1.setId(UUID.randomUUID());
        event1.setEventDate(LocalDate.of(2024, 9, 10));

        NightReception event2 = new NightReception();
        event2.setId(UUID.randomUUID());
        event2.setEventDate(LocalDate.of(2024, 9, 11));

        List<NightReception> eventList = List.of(event1, event2);

        // Simulando o comportamento do repositório
        when(nightReceptionRepository.findByMonthAndYear(month, year)).thenReturn(eventList);

        // Act
        List<NightReceptionDTO> result = nightReceptionService.findAllEventsByMonthAndYear(month, year);

        // Assert
        assertEquals(2, result.size());
        assertEquals(event1.getId(), result.get(0).receptionId());
        assertEquals(event2.getId(), result.get(1).receptionId());
        verify(nightReceptionRepository, times(1)).findByMonthAndYear(month, year);
    }

    @Test
    void testFindByMonthAndYearFails_emptyResult() {
        // Arrange
        int month = 9;
        int year = 2024;

        // Simulando uma lista vazia de eventos retornada pelo repositório
        when(nightReceptionRepository.findByMonthAndYear(month, year)).thenReturn(Collections.emptyList());

        // Act
        List<NightReceptionDTO> result = nightReceptionService.findAllEventsByMonthAndYear(month, year);

        // Assert
        assertTrue(result.isEmpty());
        verify(nightReceptionRepository, times(1)).findByMonthAndYear(month, year);
    }

    @Test
    void testFindByMonthAndYearFails_invalidParameters() {
        // Arrange
        int invalidMonth = 13; // Mês inválido (fora do intervalo 1-12)
        int year = 2024;

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> nightReceptionService.findAllEventsByMonthAndYear(invalidMonth, year));

        assertEquals("Mês inválido", thrown.getMessage());
        verify(nightReceptionRepository, never()).findByMonthAndYear(anyInt(), anyInt());
    }

    @Test
    void findAllEventsByYear_success() {
        // Arrange
        int year = 2024;

        NightReception event1 = new NightReception();
        event1.setId(UUID.randomUUID());
        event1.setEventDate(LocalDate.of(2024, 5, 10));

        NightReception event2 = new NightReception();
        event2.setId(UUID.randomUUID());
        event2.setEventDate(LocalDate.of(2024, 8, 15));

        List<NightReception> eventList = List.of(event1, event2);

        // Simulando o comportamento do repositório
        when(nightReceptionRepository.findByYear(year)).thenReturn(eventList);

        // Act
        List<NightReceptionDTO> result = nightReceptionService.findAllEventsByYear(year);

        // Assert
        assertEquals(2, result.size());
        assertEquals(event1.getId(), result.get(0).receptionId());
        assertEquals(event2.getId(), result.get(1).receptionId());
        verify(nightReceptionRepository, times(1)).findByYear(year);
    }

    @Test
    void findAllEventsByYear_emptyResult() {
        // Arrange
        int year = 2025;

        // Simulando uma lista vazia de eventos retornada pelo repositório
        when(nightReceptionRepository.findByYear(year)).thenReturn(Collections.emptyList());

        // Act
        List<NightReceptionDTO> result = nightReceptionService.findAllEventsByYear(year);

        // Assert
        assertTrue(result.isEmpty());
        verify(nightReceptionRepository, times(1)).findByYear(year);
    }

    @Test
    void findAllEventsByYear_invalidYear() {
        // Arrange
        int invalidYear = -1; // Ano inválido (negativo)

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> nightReceptionService.findAllEventsByYear(invalidYear));

        assertEquals("Ano inválido", thrown.getMessage());
        verify(nightReceptionRepository, never()).findByYear(anyInt());
    }






}
