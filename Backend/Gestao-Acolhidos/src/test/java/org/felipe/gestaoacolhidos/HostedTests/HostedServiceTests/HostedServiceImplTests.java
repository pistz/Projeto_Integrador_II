package org.felipe.gestaoacolhidos.HostedTests.HostedServiceTests;

import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedCreateNewDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseCreatedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseDeletedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseUpdatedDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.domain.service.Hosted.HostedServiceImpl;
import org.felipe.gestaoacolhidos.model.exceptions.HostedAlreadyRegisteredException;
import org.felipe.gestaoacolhidos.model.logs.UserLoggingInterceptor;
import org.felipe.gestaoacolhidos.model.repository.hosted.HostedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HostedServiceImplTests {

    @Mock
    private HostedRepository hostedRepository;

    @Mock
    private UserLoggingInterceptor interceptor;

    @InjectMocks
    private HostedServiceImpl hostedService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private boolean mockValidateCPF(String cpf) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Obtém o método privado validateCPF da classe HostedServiceImpl
        Method method = HostedServiceImpl.class.getDeclaredMethod("validateCPF", String.class);
        method.setAccessible(true);

        // Cria uma instância da classe HostedServiceImpl
        HostedServiceImpl hostedService = new HostedServiceImpl();

        // Invoca o método privado validateCPF
        return (boolean) method.invoke(hostedService, cpf);
    }


    @Test
    void testCreate_Success() {

        HostedCreateNewDTO hostedCreateNewDTO = mock(HostedCreateNewDTO.class);
        when(hostedCreateNewDTO.socialSecurityNumber()).thenReturn("123.123.123-12");
        when(hostedCreateNewDTO.firstName()).thenReturn("John");
        when(hostedCreateNewDTO.lastName()).thenReturn("Doe");
        when(hostedCreateNewDTO.birthDay()).thenReturn(LocalDate.of(1990, 1, 1));
        when(hostedRepository.existsBySocialSecurityNumber("123.123.123-12")).thenReturn(false);
        when(hostedCreateNewDTO.paperTrail()).thenReturn(1L);
        when(hostedCreateNewDTO.fathersName()).thenReturn("Father");
        when(hostedCreateNewDTO.mothersName()).thenReturn("Mother");
        when(hostedCreateNewDTO.occupation()).thenReturn("None");
        when(hostedCreateNewDTO.cityOrigin()).thenReturn("City");
        when(hostedCreateNewDTO.stateOrigin()).thenReturn("State");

        HostedResponseCreatedDTO response = hostedService.create(hostedCreateNewDTO);

        assertNotNull(response);
        assertEquals("Acolhido registrado", response.message());
        verify(hostedRepository).save(any(Hosted.class));
    }

    @Test
    void testCreate_InvalidCPF() {

        HostedCreateNewDTO hostedCreateNewDTO = mock(HostedCreateNewDTO.class);
        when(hostedCreateNewDTO.socialSecurityNumber()).thenReturn("00000000000");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            hostedService.create(hostedCreateNewDTO);
        });

        assertEquals("CPF invalido", exception.getMessage());
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testCreate_HostedAlreadyRegistered() {

        HostedCreateNewDTO hostedCreateNewDTO = mock(HostedCreateNewDTO.class);
        when(hostedCreateNewDTO.socialSecurityNumber()).thenReturn("123.123.123-12");
        when(hostedRepository.existsBySocialSecurityNumber("123.123.123-12")).thenReturn(true);

        HostedAlreadyRegisteredException exception = assertThrows(HostedAlreadyRegisteredException.class, () -> {
            hostedService.create(hostedCreateNewDTO);
        });

        assertEquals("Acolhido já possui registro ativo", exception.getMessage());
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testDelete_Success() {

        UUID id = UUID.randomUUID();
        when(hostedRepository.existsById(id)).thenReturn(true);

        HostedResponseDeletedDTO response = hostedService.delete(id);

        assertNotNull(response);
        assertEquals("Registro deletado", response.message());
        verify(hostedRepository).deleteById(id);
    }

    @Test
    void testDelete_IdNotExists() {

        UUID id = UUID.randomUUID();
        when(hostedRepository.existsById(id)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            hostedService.delete(id);
        });

        assertEquals("Id não existe", exception.getMessage());
        verify(hostedRepository, never()).deleteById(id);
    }

    @Test
    void testFindById_Success() {

        UUID id = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        when(hostedRepository.findById(id)).thenReturn(Optional.of(hosted));

        Hosted result = hostedService.findById(id);

        assertNotNull(result);
        assertEquals(hosted, result);
        verify(hostedRepository).findById(id);
    }

    @Test
    void testFindById_NotFound() {

        UUID id = UUID.randomUUID();
        when(hostedRepository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            hostedService.findById(id);
        });

        assertNotNull(exception);
        verify(hostedRepository).findById(id);
    }

    @Test
    void testFindAll_Success() {

        List<Hosted> hostedList = List.of(Hosted.builder().build(), Hosted.builder().build());
        when(hostedRepository.findAll()).thenReturn(hostedList);

        List<Hosted> result = hostedService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(hostedRepository).findAll();
    }

    @Test
    void testFindAll_EmptyList() {

        when(hostedRepository.findAll()).thenReturn(List.of());

        List<Hosted> result = hostedService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(hostedRepository).findAll();
    }

    @Test
    void testUpdateIdentification_Success() {
        // Cenário feliz - Atualização bem-sucedida
        UUID id = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder().build();
        HostedCreateNewDTO updateDTO = new HostedCreateNewDTO(
                "Novo Nome",
                "Novo Sobrenome",
                "123.456.789-00",
                LocalDate.of(1980, 1, 1),
                1L,
                "Nome do Pai",
                "Nome da Mãe",
                "Ocupação",
                "Cidade",
                "Estado"
        );

        when(hostedRepository.findById(id)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateIdentification(id, updateDTO);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());
        assertEquals(updateDTO.socialSecurityNumber(), existingHosted.getSocialSecurityNumber());
        assertEquals(updateDTO.firstName(), existingHosted.getFirstName());
        assertEquals(updateDTO.lastName(), existingHosted.getLastName());
        assertEquals(updateDTO.paperTrail(), existingHosted.getPaperTrail());
        assertEquals(updateDTO.birthDay(), existingHosted.getBirthDay());
        assertEquals(updateDTO.mothersName(), existingHosted.getMothersName());
        assertEquals(updateDTO.fathersName(), existingHosted.getFathersName());
        assertEquals(updateDTO.occupation(), existingHosted.getOccupation());
        assertEquals(updateDTO.cityOrigin(), existingHosted.getCityOrigin());
        assertEquals(updateDTO.stateOrigin(), existingHosted.getStateOrigin());
        assertEquals(LocalDate.now(), existingHosted.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());
        verify(hostedRepository).save(existingHosted);
    }


}
