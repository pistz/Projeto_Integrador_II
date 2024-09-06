package org.felipe.gestaoacolhidos.HostedTests.HostedServiceTests;

import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.CustomTreatments.CustomTreatmentsDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents.DocumentsBirthCertificateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.FamilyComposition.FamilyCompositionDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.FamilyComposition.FamilyTableMemberDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedCreateNewDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseCreatedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseDeletedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseUpdatedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.MedicalRecord.MedicalRecordDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.PoliceReport.PoliceReportDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.ReferenceAddress.ReferenceAddressDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.SituationalRisk.SituationalRiskUpdateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.SocialPrograms.SocialProgramsDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.BirthCertificate.BirthCertificate;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.CustomTreatments.CustomTreatments;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Documents.Documents;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyComposition.FamilyComposition;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyTable.FamilyTable;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.MedicalRecord.MedicalRecord;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.PoliceReport.PoliceReport;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.ReferenceAddress.ReferenceAddress;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SituationalRisk.SituationalRisk;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SocialPrograms.SocialPrograms;
import org.felipe.gestaoacolhidos.model.domain.enums.education.Education;
import org.felipe.gestaoacolhidos.model.domain.enums.gender.Gender;
import org.felipe.gestaoacolhidos.model.domain.enums.homeless.Homeless;
import org.felipe.gestaoacolhidos.model.domain.enums.lookup.LookUp;
import org.felipe.gestaoacolhidos.model.domain.enums.maritalStatus.MaritalStatus;
import org.felipe.gestaoacolhidos.model.domain.enums.migrant.Migrant;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

    @Test
    void testUpdateSituationalRisk_Success_NewSituationalRisk() {
        // Cenário feliz - Criação e atualização de novo risco situacional
        UUID hostedId = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder().build();
        SituationalRiskUpdateDTO dto = new SituationalRiskUpdateDTO(
                LookUp.ABORDAGEM_DE_RUA,
                Migrant.PASSAGEM,
                Homeless.PERNOITE
        );

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateSituationalRisk(hostedId, dto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());

        // Verificar se o risco situacional foi criado e atualizado corretamente
        SituationalRisk situationalRisk = existingHosted.getSituationalRisk();
        assertNotNull(situationalRisk);
        assertEquals(LookUp.ABORDAGEM_DE_RUA, situationalRisk.getLookUp());
        assertEquals(Migrant.PASSAGEM, situationalRisk.getMigrant());
        assertEquals(Homeless.PERNOITE, situationalRisk.getHomeless());
        assertEquals(LocalDate.now(), situationalRisk.getUpdatedAt());

        // Verificar a data de atualização e o usuário responsável
        assertEquals(LocalDate.now(), existingHosted.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());

        // Verificar que o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdateSituationalRisk_Success_ExistingSituationalRisk() {
        // Cenário feliz - Atualização de risco situacional já existente
        UUID hostedId = UUID.randomUUID();
        SituationalRisk existingRisk = new SituationalRisk();
        existingRisk.setId(UUID.randomUUID());
        Hosted existingHosted = Hosted.builder()
                .situationalRisk(existingRisk)
                .build();

        SituationalRiskUpdateDTO dto = new SituationalRiskUpdateDTO(
                LookUp.ESPONTANEA,
                Migrant.OUTRO,
                Homeless.REFEICAO
        );

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateSituationalRisk(hostedId, dto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());

        // Verificar se o risco situacional foi atualizado corretamente
        SituationalRisk situationalRisk = existingHosted.getSituationalRisk();
        assertNotNull(situationalRisk);
        assertEquals(LookUp.ESPONTANEA, situationalRisk.getLookUp());
        assertEquals(Migrant.OUTRO, situationalRisk.getMigrant());
        assertEquals(Homeless.REFEICAO, situationalRisk.getHomeless());
        assertEquals(LocalDate.now(), situationalRisk.getUpdatedAt());

        // Verificar a data de atualização e o usuário responsável
        assertEquals(LocalDate.now(), existingHosted.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());

        // Verificar que o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdateSituationalRisk_HostedNotFound() {
        // Cenário negativo - Hosted não encontrado
        UUID hostedId = UUID.randomUUID();
        SituationalRiskUpdateDTO dto = new SituationalRiskUpdateDTO(
                LookUp.FORMAL,
                Migrant.FIXAR_RESIDENCIA,
                Homeless.OUTRO
        );

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.empty());

        // Verificar se a exceção NoSuchElementException é lançada
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateSituationalRisk(hostedId, dto);
        });

        assertEquals("Acolhido não existe", thrown.getMessage());

        // Verificar que o método save não foi chamado
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateHasFamily_Success_NewFamilyComposition() {
        // Cenário feliz - Criação e atualização de nova composição familiar
        UUID hostedId = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder().build();
        FamilyCompositionDTO dto = new FamilyCompositionDTO(true, true);

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateHasFamily(hostedId, dto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());

        // Verificar se a composição familiar foi criada e atualizada corretamente
        FamilyComposition familyComposition = existingHosted.getFamilyComposition();
        assertNotNull(familyComposition);
        assertEquals(dto.hasFamily(), familyComposition.isHasFamily());
        assertEquals(dto.hasFamilyBond(), familyComposition.isHasFamilyBond());
        assertEquals(LocalDate.now(), familyComposition.getUpdatedAt());

        // Verificar a data de atualização e o usuário responsável
        assertEquals(LocalDate.now(), existingHosted.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());

        // Verificar que o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdateHasFamily_Success_ExistingFamilyComposition() {
        // Cenário feliz - Atualização de composição familiar já existente
        UUID hostedId = UUID.randomUUID();
        FamilyComposition existingFamilyComposition = new FamilyComposition();
        existingFamilyComposition.setId(UUID.randomUUID());
        Hosted existingHosted = Hosted.builder()
                .familyComposition(existingFamilyComposition)
                .build();

        FamilyCompositionDTO dto = new FamilyCompositionDTO(false, true);

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateHasFamily(hostedId, dto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());

        // Verificar se a composição familiar foi atualizada corretamente
        FamilyComposition familyComposition = existingHosted.getFamilyComposition();
        assertNotNull(familyComposition);
        assertEquals(dto.hasFamily(), familyComposition.isHasFamily());
        assertEquals(dto.hasFamilyBond(), familyComposition.isHasFamilyBond());
        assertEquals(LocalDate.now(), familyComposition.getUpdatedAt());

        // Verificar a data de atualização e o usuário responsável
        assertEquals(LocalDate.now(), existingHosted.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());

        // Verificar que o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdateHasFamily_HostedNotFound() {
        // Cenário negativo - Hosted não encontrado
        UUID hostedId = UUID.randomUUID();
        FamilyCompositionDTO dto = new FamilyCompositionDTO(true, false);

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.empty());

        // Verificar se a exceção NoSuchElementException é lançada
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateHasFamily(hostedId, dto);
        });

        assertEquals("Acolhido não existe", thrown.getMessage());

        // Verificar que o método save não foi chamado
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateFamilyTable_Success_NewFamilyTable() {
        // Cenário feliz - Criação de nova lista de membros da família
        UUID hostedId = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder()
                .familyComposition(new FamilyComposition(UUID.randomUUID(), true, true,LocalDate.now())) // tem família e vínculo familiar
                .build();

        FamilyTableMemberDTO member1 = new FamilyTableMemberDTO("Nome 1", 30, Gender.MALE, MaritalStatus.SOLTEIRO, Education.ENSINO_SUPERIOR, "Trabalho1");
        FamilyTableMemberDTO member2 = new FamilyTableMemberDTO("Nome 2", 25, Gender.FEMALE, MaritalStatus.CASADO, Education.ENSINO_MEDIO, "Trabalho2");

        List<FamilyTableMemberDTO> memberListDto = List.of(member1, member2);

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateFamilyTable(hostedId, memberListDto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());

        List<FamilyTable> familyTableList = existingHosted.getFamilyTable();
        assertNotNull(familyTableList);
        assertEquals(2, familyTableList.size());

        // Verificar os detalhes dos membros da família
        FamilyTable familyMember1 = familyTableList.get(0);
        assertEquals(member1.name(), familyMember1.getName());
        assertEquals(member1.age(), familyMember1.getAge());
        assertEquals(member1.gender(), familyMember1.getGender());
        assertEquals(member1.maritalStatus(), familyMember1.getMaritalStatus());
        assertEquals(member1.occupation(), familyMember1.getOccupation());
        assertEquals(member1.education(), familyMember1.getEducation());

        FamilyTable familyMember2 = familyTableList.get(1);
        assertEquals(member2.name(), familyMember2.getName());
        assertEquals(member2.age(), familyMember2.getAge());
        assertEquals(member2.gender(), familyMember2.getGender());
        assertEquals(member2.maritalStatus(), familyMember2.getMaritalStatus());
        assertEquals(member2.occupation(), familyMember2.getOccupation());
        assertEquals(member2.education(), familyMember2.getEducation());

        // Verificar a data de atualização e o usuário responsável
        assertEquals(LocalDate.now(), existingHosted.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());

        // Verificar que o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdateFamilyTable_Success_ExistingFamilyTable() {
        // Cenário feliz - Atualização de lista de membros da família existente
        UUID hostedId = UUID.randomUUID();
        FamilyComposition familyComposition = new FamilyComposition(UUID.randomUUID(), true, true,LocalDate.now()); // tem família e vínculo familiar
        List<FamilyTable> existingFamilyTable = new ArrayList<>();
        existingFamilyTable.add(new FamilyTable(UUID.randomUUID(), "Nome Antigo", 25, Gender.FEMALE, MaritalStatus.CASADO, Education.ENSINO_MEDIO, "Trabalho1", LocalDate.now()));

        Hosted existingHosted = Hosted.builder()
                .familyComposition(familyComposition)
                .familyTable(existingFamilyTable)
                .build();

        FamilyTableMemberDTO memberDto = new FamilyTableMemberDTO("Novo Nome", 28, Gender.MALE, MaritalStatus.SOLTEIRO, Education.ENSINO_BASICO, "Trabalho2");

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateFamilyTable(hostedId, List.of(memberDto));

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());

        // Verificar se o novo membro foi adicionado à tabela de família existente
        List<FamilyTable> familyTableList = existingHosted.getFamilyTable();
        assertEquals(2, familyTableList.size());

        FamilyTable newMember = familyTableList.get(1);
        assertEquals(memberDto.name(), newMember.getName());
        assertEquals(memberDto.age(), newMember.getAge());
        assertEquals(memberDto.gender(), newMember.getGender());
        assertEquals(memberDto.maritalStatus(), newMember.getMaritalStatus());
        assertEquals(memberDto.occupation(), newMember.getOccupation());
        assertEquals(memberDto.education(), newMember.getEducation());

        // Verificar a data de atualização e o usuário responsável
        assertEquals(LocalDate.now(), existingHosted.getUpdatedAt());
        assertEquals("Usuário Atualizador", existingHosted.getUpdatedBy());

        // Verificar que o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdateFamilyTable_HostedNotFound() {
        // Cenário negativo - Hosted não encontrado
        UUID hostedId = UUID.randomUUID();
        FamilyTableMemberDTO memberDto = new FamilyTableMemberDTO("Novo Nome", 28, Gender.MALE, MaritalStatus.SOLTEIRO, Education.ANALFABETO, "Trabalho3");

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.empty());

        // Verificar se a exceção NoSuchElementException é lançada
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateFamilyTable(hostedId, List.of(memberDto));
        });

        assertEquals("Acolhido não existe", thrown.getMessage());

        // Verificar que o método save não foi chamado
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateFamilyTable_FamilyCompositionIsNull() {
        // Cenário negativo - Hosted sem composição familiar ou vínculo familiar
        UUID hostedId = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder()
                 // sem família e sem vínculo
                .build();
        FamilyTableMemberDTO memberDto = new FamilyTableMemberDTO("Nome", 28, Gender.OTHER, MaritalStatus.DIVORCIADO, Education.POS_GRADUACAO, "Trabalho4");

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));

        // Verificar se a exceção NoSuchElementException é lançada
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateFamilyTable(hostedId, List.of(memberDto));
        });

        assertEquals("Acolhido foi marcado como sem vínculo familiar, atualize a existência de vínculo familiar", thrown.getMessage());

        // Verificar que o método save não foi chamado
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateFamilyTable_NoFamilyCompositionNoBond(){
        // Cenário negativo - Hosted sem composição familiar ou vínculo familiar
        UUID hostedId = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder()
                .familyComposition(new FamilyComposition(UUID.randomUUID(), false, false, LocalDate.now()))
                .build();
        FamilyTableMemberDTO memberDto = new FamilyTableMemberDTO("Nome", 28, Gender.OTHER, MaritalStatus.DIVORCIADO, Education.POS_GRADUACAO, "Trabalho4");

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));

        // Verificar se a exceção NoSuchElementException é lançada
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateFamilyTable(hostedId, List.of(memberDto));
        });

        assertEquals("Acolhido foi marcado como sem vínculo familiar, atualize a existência de vínculo familiar", thrown.getMessage());

        // Verificar que o método save não foi chamado
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateFamilyTable_EmptyDtoList() {
        // Cenário negativo - DTO lista vazia
        UUID hostedId = UUID.randomUUID();

        // Mock para garantir que o hosted existe com vínculos familiares
        Hosted existingHosted = Hosted.builder()
                .familyComposition(new FamilyComposition(UUID.randomUUID(),true, true, LocalDate.now())) // tem família e vínculo familiar
                .build();

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));

        // Chamar o método com uma lista vazia de membros da família
        HostedResponseUpdatedDTO response = hostedService.updateFamilyTable(hostedId, new ArrayList<>());

        // Verificar que a resposta está correta, mesmo com lista vazia
        assertNotNull(response);
        assertEquals("Registro vazio", response.message());

        // Verificar que o método save foi chamado, mas sem alterações
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdatePoliceReport_Success() {

        UUID hostedId = UUID.randomUUID();
        Hosted existingHosted = Hosted.builder().build(); // Host existente sem policeReport
        PoliceReportDTO policeReportDTO = new PoliceReportDTO(
                "123456", "Departamento de Polícia", "São Paulo", "Relatório detalhado"
        );

        // Simulando o retorno do banco de dados com um registro existente
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        // Chamando o método a ser testado
        HostedResponseUpdatedDTO response = hostedService.updatePoliceReport(hostedId, policeReportDTO);

        // Verificações
        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());
        assertNotNull(existingHosted.getPoliceReport());
        assertEquals(1, existingHosted.getPoliceReport().size());

        PoliceReport addedPoliceReport = existingHosted.getPoliceReport().get(0);
        assertEquals(policeReportDTO.reportProtocol(), addedPoliceReport.getReportProtocol());
        assertEquals(policeReportDTO.policeDepartment(), addedPoliceReport.getPoliceDepartment());
        assertEquals(policeReportDTO.city(), addedPoliceReport.getCity());
        assertEquals(policeReportDTO.reportInfo(), addedPoliceReport.getReportInfo());
        assertEquals(LocalDate.now(), addedPoliceReport.getCreatedAt());

        // Verifica se o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdatePoliceReport_HostedNotFound() {
        // Cenário negativo - HostedId não encontrado
        UUID hostedId = UUID.randomUUID();
        PoliceReportDTO policeReportDTO = new PoliceReportDTO(
                "123456", "Departamento de Polícia", "São Paulo", "Relatório detalhado"
        );

        // Simulando o retorno do banco de dados como vazio
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.empty());

        // Verificando a exceção lançada
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            hostedService.updatePoliceReport(hostedId, policeReportDTO);
        });

        assertEquals("Acolhido não existe", thrown.getMessage());

        // Verifica que o método save não foi chamado
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdatePoliceReport_ExistingPoliceReport() {
        // Cenário - Hosted já possui um relatório de polícia
        UUID hostedId = UUID.randomUUID();
        PoliceReport existingReport = new PoliceReport();
        existingReport.setId(UUID.randomUUID());
        existingReport.setReportProtocol("654321");
        existingReport.setPoliceDepartment("Outro Departamento");
        existingReport.setCity("Outra Cidade");
        existingReport.setReportInfo("Informações anteriores");

        Hosted existingHosted = Hosted.builder()
                .policeReport(new ArrayList<>(List.of(existingReport)))
                .build();

        PoliceReportDTO policeReportDTO = new PoliceReportDTO(
                "123456", "Departamento de Polícia", "São Paulo", "Relatório detalhado"
        );

        // Simulando o retorno do banco de dados com um registro existente
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        // Chamando o método a ser testado
        HostedResponseUpdatedDTO response = hostedService.updatePoliceReport(hostedId, policeReportDTO);

        // Verificações
        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());
        assertEquals(2, existingHosted.getPoliceReport().size());

        PoliceReport addedPoliceReport = existingHosted.getPoliceReport().get(1);
        assertEquals(policeReportDTO.reportProtocol(), addedPoliceReport.getReportProtocol());
        assertEquals(policeReportDTO.policeDepartment(), addedPoliceReport.getPoliceDepartment());
        assertEquals(policeReportDTO.city(), addedPoliceReport.getCity());
        assertEquals(policeReportDTO.reportInfo(), addedPoliceReport.getReportInfo());
        assertEquals(LocalDate.now(), addedPoliceReport.getCreatedAt());

        // Verifica se o método save foi chamado
        verify(hostedRepository).save(existingHosted);
    }

    @Test
    void testUpdatePoliceReport_InvalidDTO() {
        // Cenário negativo - DTO inválido (campos nulos ou vazios)
        UUID hostedId = UUID.randomUUID();
        PoliceReportDTO policeReportDTO = new PoliceReportDTO(
                null, null, null, null
        );

        Hosted existingHosted = Hosted.builder().build();

        // Simulando o retorno do banco de dados com um registro existente
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(existingHosted));

        // Chamando o método a ser testado
        HostedResponseUpdatedDTO response = hostedService.updatePoliceReport(hostedId, policeReportDTO);

        // Verificações
        assertNotNull(response);
        assertEquals("Registro vazio", response.message());

        // O relatório de polícia não deve ser acionado com valores nulos
        assertNull(existingHosted.getPoliceReport());
        // Verifica se o método save foi chamado
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateReferenceAddress_Success_NewReference() {
        // Cenário feliz - novo endereço de referência
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");
        ReferenceAddressDTO dto = new ReferenceAddressDTO("Rua A", "Cidade B", "Bairro C", 123, "12345-678", 987654321);


        HostedResponseUpdatedDTO response = hostedService.updateReferenceAddress(hostedId, dto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());
        assertNotNull(hosted.getReferenceAddress());
        assertEquals(dto.street(), hosted.getReferenceAddress().getStreet());
        assertEquals(dto.city(), hosted.getReferenceAddress().getCidade());
        assertEquals(dto.neighborhood(), hosted.getReferenceAddress().getNeighborhood());
        assertEquals(dto.number(), hosted.getReferenceAddress().getNumber());
        assertEquals(dto.cep(), hosted.getReferenceAddress().getCep());
        assertEquals(dto.phoneNumber(), hosted.getReferenceAddress().getPhoneNumber());
        assertEquals(LocalDate.now(), hosted.getReferenceAddress().getUpdatedAt());
        assertEquals("Usuário Atualizador", hosted.getUpdatedBy());

        verify(hostedRepository).save(hosted);
    }

    @Test
    void testUpdateReferenceAddress_Success_ExistingReference() {
        // Cenário feliz - endereço de referência já existente
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        ReferenceAddressDTO dto = new ReferenceAddressDTO("Rua A", "Cidade B", "Bairro C", 123, "12345-678", 987654321);

        ReferenceAddress existingAddress = new ReferenceAddress();
        existingAddress.setStreet("Old Street");
        existingAddress.setNeighborhood("Old Neighborhood");
        hosted.setReferenceAddress(existingAddress);

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));
        when(interceptor.getRegisteredUser()).thenReturn("Usuário Atualizador");

        HostedResponseUpdatedDTO response = hostedService.updateReferenceAddress(hostedId, dto);

        assertNotNull(response);
        assertEquals("Registro atualizado", response.message());
        assertEquals(dto.street(), hosted.getReferenceAddress().getStreet());
        assertEquals(dto.city(), hosted.getReferenceAddress().getCidade());
        assertEquals(dto.neighborhood(), hosted.getReferenceAddress().getNeighborhood());
        assertEquals(dto.number(), hosted.getReferenceAddress().getNumber());
        assertEquals(dto.cep(), hosted.getReferenceAddress().getCep());
        assertEquals(dto.phoneNumber(), hosted.getReferenceAddress().getPhoneNumber());
        assertEquals(LocalDate.now(), hosted.getReferenceAddress().getUpdatedAt());
        assertEquals("Usuário Atualizador", hosted.getUpdatedBy());

        verify(hostedRepository).save(hosted);
    }

    @Test
    void testUpdateReferenceAddress_EmptyDTO() {
        // Cenário negativo - DTO vazio
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();

        ReferenceAddressDTO emptyDto = null;

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));

        HostedResponseUpdatedDTO response = hostedService.updateReferenceAddress(hostedId, emptyDto);

        assertNotNull(response);
        assertEquals("Registro vazio", response.message());
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateReferenceAddress_HostedNotFound() {
        // Cenário negativo - acolhido não encontrado
        UUID hostedId = UUID.randomUUID();
        ReferenceAddressDTO dto = new ReferenceAddressDTO("Rua A", "Cidade B", "Bairro C", 123, "12345-678", 987654321);
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateReferenceAddress(hostedId, dto);
        });

        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateSocialPrograms_Success() {

        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        SocialPrograms socialPrograms = new SocialPrograms();
        SocialProgramsDTO dto = new SocialProgramsDTO(
                true, true, true, false, false, true, false, true,
                "Other programs", 5, new BigDecimal("1200.00")
        );
        // Cenário feliz - dados válidos e registro atualizado com sucesso
        hosted.setSocialPrograms(socialPrograms);
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));
        when(interceptor.getRegisteredUser()).thenReturn("Admin");

        HostedResponseUpdatedDTO response = hostedService.updateSocialPrograms(hostedId, dto);

        assertEquals("Registro atualizado", response.message());
        assertNotNull(hosted.getSocialPrograms());
        assertTrue(hosted.getSocialPrograms().isHasPasseDeficiente());
        assertEquals(5, hosted.getSocialPrograms().getHowLong());
        assertEquals("Other programs", hosted.getSocialPrograms().getOthers());
        verify(hostedRepository).save(hosted);
    }

    @Test
    void testUpdateSocialPrograms_NullDto() {
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        // Cenário negativo - DTO nulo ou com todas as propriedades nulas
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));

        SocialProgramsDTO emptyDto = null;
        HostedResponseUpdatedDTO response = hostedService.updateSocialPrograms(hostedId, emptyDto);

        assertEquals("Registro vazio", response.message());
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateSocialPrograms_NewSocialPrograms() {
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        SocialProgramsDTO dto = new SocialProgramsDTO(
                true, true, true, false, false, true, false, true,
                "Other programs", 5, new BigDecimal("1200.00")
        );
        // Cenário - socialPrograms nulo no Hosted, criando um novo registro de SocialPrograms
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));
        when(interceptor.getRegisteredUser()).thenReturn("Admin");

        HostedResponseUpdatedDTO response = hostedService.updateSocialPrograms(hostedId, dto);

        assertNotNull(hosted.getSocialPrograms());
        assertEquals("Admin", hosted.getUpdatedBy());
        verify(hostedRepository).save(hosted);
        assertEquals("Registro atualizado", response.message());
    }

    @Test
    void testUpdateSocialPrograms_HostedNotFound() {
        UUID hostedId = UUID.randomUUID();
        SocialProgramsDTO dto = new SocialProgramsDTO(
                true, true, true, false, false, true, false, true,
                "Other programs", 5, new BigDecimal("1200.00")
        );

        // Cenário negativo - Hosted não encontrado
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateSocialPrograms(hostedId, dto);
        });

        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateSocialPrograms_NullSocialProgramsFields() {
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        // Cenário negativo - Verificar que os campos em SocialPrograms são corretamente atualizados mesmo com alguns valores nulos
        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));
        when(interceptor.getRegisteredUser()).thenReturn("Admin");

        SocialProgramsDTO partialDto = new SocialProgramsDTO(
                false, false, false, false, false, false, false, false,
                null, 0, null
        );

        HostedResponseUpdatedDTO response = hostedService.updateSocialPrograms(hostedId, partialDto);

        assertEquals("Registro atualizado", response.message());
        assertNotNull(hosted.getSocialPrograms());
        assertFalse(hosted.getSocialPrograms().isHasPasseDeficiente());
        assertNull(hosted.getSocialPrograms().getOthers());
        verify(hostedRepository).save(hosted);
    }

    @Test
    void testUpdateMedicalRecord_Success() {
        // Cenário feliz - todos os dados fornecidos e o registro atualizado com sucesso
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        MedicalRecordDTO dto = new MedicalRecordDTO("Dor de cabeça");

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));
        when(interceptor.getRegisteredUser()).thenReturn("Admin");

        HostedResponseUpdatedDTO response = hostedService.updateMedicalRecord(hostedId, dto);

        assertEquals("Registro atualizado", response.message());
        assertNotNull(hosted.getMedicalRecord());
        assertEquals(1, hosted.getMedicalRecord().size());
        assertEquals("Dor de cabeça", hosted.getMedicalRecord().get(0).getComplaints());
        assertEquals("Admin", hosted.getUpdatedBy());
        verify(hostedRepository).save(hosted);
    }

    @Test
    void testUpdateMedicalRecord_EmptyDTO() {
        // Cenário negativo - DTO nulo ou com todas as propriedades nulas
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        MedicalRecordDTO emptyDto = new MedicalRecordDTO(null);

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));

        HostedResponseUpdatedDTO response = hostedService.updateMedicalRecord(hostedId, emptyDto);

        assertEquals("Registro vazio", response.message());
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateMedicalRecord_NullDTO() {
        // Cenário negativo - DTO nulo ou com todas as propriedades nulas
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        MedicalRecordDTO nullDto = null;

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));

        HostedResponseUpdatedDTO response = hostedService.updateMedicalRecord(hostedId, nullDto);

        assertEquals("Registro vazio", response.message());
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateMedicalRecord_MedicalRecordAlreadyExists() {
        // Cenário - o Hosted já possui registros médicos e um novo registro é adicionado
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        MedicalRecord existingRecord = new MedicalRecord();
        existingRecord.setComplaints("Dor de estômago");
        hosted.setMedicalRecord(new ArrayList<>(Collections.singletonList(existingRecord)));

        MedicalRecordDTO dto = new MedicalRecordDTO("Gripe forte");

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));
        when(interceptor.getRegisteredUser()).thenReturn("Admin");

        HostedResponseUpdatedDTO response = hostedService.updateMedicalRecord(hostedId, dto);

        assertEquals(2, hosted.getMedicalRecord().size());
        assertEquals("Gripe forte", hosted.getMedicalRecord().get(1).getComplaints());
        verify(hostedRepository).save(hosted);
    }

    @Test
    void testUpdateCustomTreatments_Success() {
        // Cenário feliz - todos os dados fornecidos e o registro atualizado com sucesso
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        CustomTreatmentsDTO dto = new CustomTreatmentsDTO("Tratamento XYZ");

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));
        when(interceptor.getRegisteredUser()).thenReturn("Admin");

        HostedResponseUpdatedDTO response = hostedService.updateCustomTreatments(hostedId, dto);

        assertEquals("Registro atualizado", response.message());
        assertNotNull(hosted.getCustomTreatments());
        assertEquals(1, hosted.getCustomTreatments().size());
        assertEquals("Tratamento XYZ", hosted.getCustomTreatments().get(0).getProcedure());
        assertEquals("Admin", hosted.getUpdatedBy());
        verify(hostedRepository).save(hosted);
    }

    @Test
    void testUpdateCustomTreatments_EmptyDTO() {
        // Cenário negativo - DTO nulo ou com todas as propriedades nulas
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        CustomTreatmentsDTO emptyDto = new CustomTreatmentsDTO(null);

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));

        HostedResponseUpdatedDTO response = hostedService.updateCustomTreatments(hostedId, emptyDto);

        assertEquals("Registro vazio", response.message());
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateCustomTreatments_NullDTO() {
        // Cenário negativo - DTO nulo ou com todas as propriedades nulas
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        CustomTreatmentsDTO nullDto = null;

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));

        HostedResponseUpdatedDTO response = hostedService.updateCustomTreatments(hostedId, nullDto);

        assertEquals("Registro vazio", response.message());
        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateCustomTreatments_HostedNotFound() {
        // Cenário negativo - Hosted não encontrado
        UUID hostedId = UUID.randomUUID();
        CustomTreatmentsDTO dto = new CustomTreatmentsDTO("Tratamento especial");

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            hostedService.updateCustomTreatments(hostedId, dto);
        });

        verify(hostedRepository, never()).save(any(Hosted.class));
    }

    @Test
    void testUpdateCustomTreatments_CustomTreatmentsAlreadyExists() {
        // Cenário - o Hosted já possui tratamentos personalizados e um novo tratamento é adicionado
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        CustomTreatments existingTreatment = new CustomTreatments();
        existingTreatment.setProcedure("Tratamento existente");
        hosted.setCustomTreatments(new ArrayList<>(Collections.singletonList(existingTreatment)));

        CustomTreatmentsDTO dto = new CustomTreatmentsDTO("Novo tratamento");

        when(hostedRepository.findById(hostedId)).thenReturn(Optional.of(hosted));
        when(interceptor.getRegisteredUser()).thenReturn("Admin");

        HostedResponseUpdatedDTO response = hostedService.updateCustomTreatments(hostedId, dto);

        assertEquals(2, hosted.getCustomTreatments().size());
        assertEquals("Novo tratamento", hosted.getCustomTreatments().get(1).getProcedure());
        verify(hostedRepository).save(hosted);
    }
}
