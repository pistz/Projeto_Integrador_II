package org.felipe.gestaoacolhidos.HostedTests.HostedServiceTests;

import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents.DocumentsBirthCertificateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedCreateNewDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseCreatedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseDeletedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseUpdatedDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.BirthCertificate.BirthCertificate;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Documents.Documents;
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

        assertEquals("CPF inválido", exception.getMessage());
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

        UUID id = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder().socialSecurityNumber("999.999.999-99").build();
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

    @Test
    void testUpdateIdentification_HostedNotFound() {

        UUID id = UUID.randomUUID();
        HostedCreateNewDTO updateDTO = new HostedCreateNewDTO(
                "Nome",
                "Sobrenome",
                "123.456.789-00",
                LocalDate.of(1980, 1, 1),
                1L,
                "Nome do Pai",
                "Nome da Mãe",
                "Ocupação",
                "Cidade",
                "Estado"
        );

        when(hostedRepository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateIdentification(id, updateDTO);
        });

        assertNotNull(exception);
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateIdentification_InvalidCPF() {

        UUID id = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder().build();
        HostedCreateNewDTO updateDTO = new HostedCreateNewDTO(
                "Novo Nome",
                "Novo Sobrenome",
                "12345678910",
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


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hostedService.updateIdentification(id, updateDTO);
        });

        assertEquals("CPF inválido", exception.getMessage());

        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateDocuments_Success_NewDocumentsAndBirthCertificate() {

        UUID hostedId = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder().build();
        DocumentsUpdateDTO dto = new DocumentsUpdateDTO(
                "RG123456",
                LocalDate.of(2010, 5, 15),
                "CNH123456",
                new DocumentsBirthCertificateDTO(132L, "Folha123", 10)
        );

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateDocuments(hostedId, dto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());

        // Verificar se os documentos foram atualizados corretamente
        Documents documents = existingHosted.getOtherDocuments();
        assertNotNull(documents);
        assertEquals("RG123456", documents.getGeneralRegisterRG());
        assertEquals(LocalDate.of(2010, 5, 15), documents.getDateOfIssueRG());
        assertEquals("CNH123456", documents.getDriversLicenseNumber());
        assertEquals(true,documents.isHasLicense());

        BirthCertificate birthCertificate = documents.getBirthCertificate();
        assertNotNull(birthCertificate);
        assertEquals(132L, birthCertificate.getCertificateNumber());
        assertEquals("Folha123", birthCertificate.getSheets());
        assertEquals(10, birthCertificate.getBook());

        // Verificar a data de atualização
        assertEquals(LocalDate.now(), documents.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());

        // Verificar que o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdateDocuments_Success_NewDocumentsWithoutCNH() {

        UUID hostedId = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder().build();
        DocumentsUpdateDTO dto = new DocumentsUpdateDTO(
                "RG123456",
                LocalDate.of(2010, 5, 15),
                null,
                new DocumentsBirthCertificateDTO(132L, "Folha123", 10)
        );

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateDocuments(hostedId, dto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());

        // Verificar se os documentos foram atualizados corretamente
        Documents documents = existingHosted.getOtherDocuments();
        assertNotNull(documents);
        assertEquals(false,documents.isHasLicense());

        // Verificar a data de atualização
        assertEquals(LocalDate.now(), documents.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());

        // Verificar que o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdateDocuments_Success_ExistingDocuments() {
        // Cenário feliz - Atualização bem-sucedida com documentos já existentes
        UUID hostedId = UUID.randomUUID();
        Documents existingDocuments = new Documents();
        existingDocuments.setId(UUID.randomUUID());

        BirthCertificate existingBirthCertificate = new BirthCertificate();
        existingBirthCertificate.setId(UUID.randomUUID());
        existingDocuments.setBirthCertificate(existingBirthCertificate);

        Hosted existingHosted = Hosted.builder()
                .otherDocuments(existingDocuments)
                .build();

        DocumentsUpdateDTO dto = new DocumentsUpdateDTO(
                "RG987654",
                LocalDate.of(2015, 10, 20),
                "CNH654321",
                new DocumentsBirthCertificateDTO(987L, "Folha987", 987)
        );

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateDocuments(hostedId, dto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());

        // Verificar se os documentos foram atualizados corretamente
        Documents documents = existingHosted.getOtherDocuments();
        assertNotNull(documents);
        assertEquals("RG987654", documents.getGeneralRegisterRG());
        assertEquals(LocalDate.of(2015, 10, 20), documents.getDateOfIssueRG());

        assertEquals("CNH654321", documents.getDriversLicenseNumber());

        BirthCertificate birthCertificate = documents.getBirthCertificate();
        assertNotNull(birthCertificate);
        assertEquals(987L, birthCertificate.getCertificateNumber());
        assertEquals("Folha987", birthCertificate.getSheets());
        assertEquals(987, birthCertificate.getBook());
        assertEquals(true,documents.isHasLicense());

        // Verificar a data de atualização
        assertEquals(LocalDate.now(), documents.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());

        // Verificar que o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdateDocuments_HostedNotFound() {
        // Cenário negativo - Hosted não encontrado
        UUID hostedId = UUID.randomUUID();
        DocumentsUpdateDTO dto = new DocumentsUpdateDTO(
                "RG123456",
                LocalDate.of(2010, 5, 15),
                "CNH123456",
                new DocumentsBirthCertificateDTO(123L, "Folha123", 123)
        );

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.empty());

        // Verificar se a exceção NoSuchElementException é lançada
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateDocuments(hostedId, dto);
        });

        assertEquals("Acolhido não existe", thrown.getMessage());

        // Verificar que o método save não foi chamado
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

}
