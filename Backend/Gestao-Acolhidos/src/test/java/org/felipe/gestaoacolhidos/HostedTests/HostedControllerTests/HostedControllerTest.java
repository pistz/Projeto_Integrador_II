package org.felipe.gestaoacolhidos.HostedTests.HostedControllerTests;

import org.felipe.gestaoacolhidos.model.controller.ExceptionHandler.ControllerExceptionHandler;
import org.felipe.gestaoacolhidos.model.controller.Hosted.HostedController;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents.DocumentsBirthCertificateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.FamilyComposition.FamilyCompositionDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.FamilyComposition.FamilyTableMemberDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedCreateNewDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseDeletedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseUpdatedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.SituationalRisk.SituationalRiskUpdateDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.domain.enums.education.Education;
import org.felipe.gestaoacolhidos.model.domain.enums.gender.Gender;
import org.felipe.gestaoacolhidos.model.domain.enums.homeless.Homeless;
import org.felipe.gestaoacolhidos.model.domain.enums.lookup.LookUp;
import org.felipe.gestaoacolhidos.model.domain.enums.maritalStatus.MaritalStatus;
import org.felipe.gestaoacolhidos.model.domain.enums.migrant.Migrant;
import org.felipe.gestaoacolhidos.model.domain.service.Hosted.HostedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HostedControllerTest {
    private MockMvc mockMvc;

    @Mock
    private HostedService hostedService;

    @InjectMocks
    private HostedController hostedController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hostedController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void testFindAll_Success(){

        Hosted hosted1 = Hosted.builder()
                .id(UUID.randomUUID())
                .paperTrail(12345L)
                .firstName("John")
                .lastName("Doe")
                .socialSecurityNumber("123.456.789-00")
                .birthDay(LocalDate.of(1990, 1, 1))
                .createdAt(LocalDate.now())
                .build();
        hosted1.setAge(hosted1.getAge());

        Hosted hosted2 = Hosted.builder()
                .id(UUID.randomUUID())
                .paperTrail(67890L)
                .firstName("Jane")
                .lastName("Smith")
                .socialSecurityNumber("987.654.321-00")
                .birthDay(LocalDate.of(1985, 5, 20))
                .createdAt(LocalDate.now())
                .build();
        hosted2.setAge(hosted2.getAge());
        List<Hosted> hostedList = List.of(hosted1, hosted2);
        when(hostedService.findAll()).thenReturn(hostedList);
        ResponseEntity<List<Hosted>> response = hostedController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(hostedService, times(1)).findAll();
    }

    @Test
    void testFindAll_SuccessWithEmptyList(){
        List<Hosted> hostedList = List.of();
        when(hostedService.findAll()).thenReturn(hostedList);
        ResponseEntity<List<Hosted>> response = hostedController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).size());
        verify(hostedService, times(1)).findAll();
    }

    @Test
    void testFindAllEndpoint_Success() throws Exception {

        mockMvc.perform(get("/hosted/find/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindById_Success(){
        UUID hostedId = UUID.randomUUID();
        Hosted hosted = Hosted.builder().build();
        when(hostedService.findById(hostedId)).thenReturn(hosted);

        ResponseEntity<Hosted> response = hostedController.findById(hostedId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hostedService, times(1)).findById(hostedId);
    }

    @Test
    public void testFindById_Failure(){
        UUID hostedId = UUID.randomUUID();
        when(hostedService.findById(hostedId)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> hostedController.findById(hostedId));

        verify(hostedService, times(1)).findById(hostedId);
    }

    @Test
    public void testFindById_FailureWithNull(){
        UUID hostedId = null;
        when(hostedService.findById(hostedId)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> hostedController.findById(hostedId));

        verify(hostedService, times(1)).findById(hostedId);
    }

    @Test
    void testFindByIdEndpoint_Success() throws Exception {

        UUID hostedId = UUID.randomUUID();
        when(hostedService.findById(hostedId)).thenReturn(Hosted.builder().id(hostedId).build());

        mockMvc.perform(get("/hosted/find/"+hostedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByIdEndpoint_FailureDueToIdNotExists() throws Exception {
        UUID invalidId = UUID.randomUUID();
        when(hostedService.findById(any(UUID.class))).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/hosted/find/"+ invalidId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void testFindByIdEndpoint_FailureNullId() throws Exception {
        UUID invalidId = null;
        when(hostedService.findById(any(UUID.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/hosted/find/"+ invalidId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete_Success() throws Exception {
        UUID hostedId = UUID.randomUUID();
        HostedResponseDeletedDTO hosted = new HostedResponseDeletedDTO("Registro deletado");
        when(hostedService.delete(hostedId)).thenReturn(hosted);

        ResponseEntity<HostedResponseDeletedDTO> response = hostedController.deleteHosted(hostedId);
        assertEquals("Registro deletado", hosted.message());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(hostedService, times(1)).delete(hostedId);
    }

    @Test
    public void testDeleteEndpoint_Success() throws Exception {
        UUID hostedId = UUID.randomUUID();
        HostedResponseDeletedDTO hosted = new HostedResponseDeletedDTO("Registro deletado");
        when(hostedService.delete(hostedId)).thenReturn(hosted);

        mockMvc.perform(delete("/hosted/delete/" + hostedId))
            .andExpect(status().isNoContent())
            .andExpect(content().string("{\"message\":\"Registro deletado\"}"));

    }

    @Test
    public void testDelete_FailureWithWrongId() throws Exception {
        UUID hostedId = UUID.randomUUID();

        when(hostedService.delete(hostedId)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> hostedController.deleteHosted(hostedId));

        verify(hostedService, times(1)).delete(hostedId);
    }

    @Test
    public void testeDeleteEndpoint_FailureWithWrongId() throws Exception {
        UUID hostedId = UUID.randomUUID();
        when(hostedService.delete(hostedId)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(delete("/hosted/delete/" + hostedId))
            .andExpect(status().isNotFound());
        verify(hostedService, times(1)).delete(hostedId);
    }

    @Test
    public void testDelete_FailureWithNullId() throws Exception {
        UUID hostedId = null;

        when(hostedService.delete(hostedId)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> hostedController.deleteHosted(hostedId));

        verify(hostedService, times(1)).delete(hostedId);
    }

    @Test
    public void testDeleteEndpoint_FailureWithNullId() throws Exception {
        UUID hostedId = null;

        when(hostedService.delete(hostedId)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(delete("/hosted/delete/" + hostedId))
                .andExpect(status().isBadRequest());

        verify(hostedService, never()).delete(hostedId);
    }

    @Test
    public void testUpdateMainInfo_Success(){
        UUID hostedId = UUID.randomUUID();
        HostedResponseUpdatedDTO responseUpdatedDTO = new HostedResponseUpdatedDTO("Registro atualizado");
        HostedCreateNewDTO dto = new HostedCreateNewDTO(
                "Jose",
                "Fulano",
                "999.999.999-99",
                LocalDate.of(   1980,06,20),
                1L,
                "Nome do Pai",
                "Nome da mãe",
                "",
                "",
                ""
        );
        when(hostedService.updateIdentification(hostedId, dto)).thenReturn(responseUpdatedDTO);
        ResponseEntity<HostedResponseUpdatedDTO> response = hostedController.updateHostedMainInformation(hostedId, dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseUpdatedDTO, response.getBody());
        verify(hostedService, times(1)).updateIdentification(hostedId, dto);
    }

    @Test
    public void testUpdateMainInfo_FailureHostedNotFound(){
        UUID hostedId = UUID.randomUUID();
        HostedCreateNewDTO dto = new HostedCreateNewDTO(
                "Jose",
                "Fulano",
                "999.999.999-99",
                LocalDate.of(   1980,06,20),
                1L,
                "Nome do Pai",
                "Nome da mãe",
                "",
                "",
                ""
        );
        when(hostedService.updateIdentification(hostedId, dto)).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> hostedController.updateHostedMainInformation(hostedId, dto));
        verify(hostedService, times(1)).updateIdentification(hostedId, dto);
    }

    @Test
    public void testUpdateMainInfo_FailureNullHostedId(){
        UUID hostedId = null;
        HostedCreateNewDTO dto = null;
        when(hostedController.updateHostedMainInformation(hostedId, dto)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> hostedController.updateHostedMainInformation(hostedId, dto));
        verify(hostedService, times(1)).updateIdentification(hostedId, dto);
    }

    @Test
    public void testeUpdateMainInfo_SuccessWithNullDTO(){
        UUID hostedId = UUID.randomUUID();
        HostedCreateNewDTO dto = null;
        HostedResponseUpdatedDTO responseUpdatedDTO = new HostedResponseUpdatedDTO("Registro atualizado");

        when(hostedService.updateIdentification(hostedId, dto)).thenReturn(responseUpdatedDTO);
        ResponseEntity<HostedResponseUpdatedDTO> response = hostedController
                .updateHostedMainInformation(hostedId, dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Registro atualizado", response.getBody().message());
        verify(hostedService, times(1)).updateIdentification(hostedId, dto);
    }

    @Test
    public void testeUpdateMainInfoEndpoint_Success() throws Exception {
        UUID hostedId = UUID.randomUUID();
        String dtoJson = "{"
                + "\"firstName\":\"Jose\","
                + "\"lastName\":\"Fulano\","
                + "\"socialSecurityNumber\":\"999.999.999-99\","
                + "\"birthDay\":\"1980-06-20\","
                + "\"paperTrail\":1,"
                + "\"fathersName\":\"Nome do Pai\","
                + "\"mothersName\":\"Nome da mãe\","
                + "\"occupation\":\"\","
                + "\"cityOrigin\":\"\","
                + "\"stateOrigin\":\"\""
                + "}";

        mockMvc.perform(put("/hosted/update-main-info/" + hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testeUpdateDocuments_Success(){
        UUID hostedId = UUID.randomUUID();
        DocumentsBirthCertificateDTO certificateDTO = new DocumentsBirthCertificateDTO(1L,"2AB",10);
        DocumentsUpdateDTO dto = new DocumentsUpdateDTO("4444444", LocalDate.of(2001,01,01), "55555",certificateDTO);
        HostedResponseUpdatedDTO updated = new HostedResponseUpdatedDTO("Registro atualizado");
        when(hostedService.updateDocuments(hostedId, dto)).thenReturn(updated);

        ResponseEntity<HostedResponseUpdatedDTO> response = hostedController.updateHostedDocuments(hostedId, dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(hostedService, times(1)).updateDocuments(hostedId, dto);
    }

    @Test
    public void testeUpdateDocuments_Fail_InvalidHostedId(){
        UUID hostedId = UUID.randomUUID();
        DocumentsBirthCertificateDTO certificateDTO = new DocumentsBirthCertificateDTO(1L,"2AB",10);
        DocumentsUpdateDTO dto = new DocumentsUpdateDTO("4444444", LocalDate.of(2001,01,01), "55555",certificateDTO);
        when(hostedService.updateDocuments(hostedId, dto)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> hostedController.updateHostedDocuments(hostedId, dto));
        verify(hostedService, times(1)).updateDocuments(hostedId, dto);
    }

    @Test
    public void testeUpdateDocuments_Fail_NullHostId(){
        UUID hostedId = null;
        DocumentsBirthCertificateDTO certificateDTO = new DocumentsBirthCertificateDTO(1L,"2AB",10);
        DocumentsUpdateDTO dto = new DocumentsUpdateDTO("4444444", LocalDate.of(2001,01,01), "55555",certificateDTO);
        when(hostedService.updateDocuments(hostedId, dto)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> hostedController.updateHostedDocuments(hostedId, dto));
        verify(hostedService, times(1)).updateDocuments(hostedId, dto);
    }

    @Test
    public void testeUpdateDocuments_Success_NullDTO(){
        UUID hostedId = null;

        DocumentsUpdateDTO dto = null;
        HostedResponseUpdatedDTO DTOresponse = new HostedResponseUpdatedDTO("Registro vazio");
        when(hostedService.updateDocuments(hostedId, dto)).thenReturn(DTOresponse);

        ResponseEntity<HostedResponseUpdatedDTO> response = hostedController.updateHostedDocuments(hostedId, dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(DTOresponse, response.getBody());
        verify(hostedService, times(1)).updateDocuments(hostedId, dto);
    }

    @Test
    public void testeUpdateDocumentsEndpoint_Success() throws Exception {
        UUID hostedId = UUID.randomUUID();
        String dtoJson = "{"
                + "\"generalRegister\":\"444444\","
                + "\"dateOfIssueRG\":\"1900-01-01\","
                + "\"driversLicenseNumber\":\"555555\","
                + "\"birthCertificate\":{"
                + "\"certificateNumber\":1,"
                + "\"sheets\":\"2A\","
                + "\"book\":2"
                + "}"
                + "}";

        mockMvc.perform(put("/hosted/update-docs/" + hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testeUpdateDocumentsEndpoint_FailsWithWrongId() throws Exception {
        UUID hostedId = UUID.randomUUID();
        String dtoJson = "{"
                + "\"generalRegister\":\"444444\","
                + "\"dateOfIssueRG\":\"1900-01-01\","
                + "\"driversLicenseNumber\":\"555555\","
                + "\"birthCertificate\":{"
                + "\"certificateNumber\":1,"
                + "\"sheets\":\"2A\","
                + "\"book\":2"
                + "}"
                + "}";

        when(hostedController.updateHostedDocuments(eq(hostedId),any(DocumentsUpdateDTO.class))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(put("/hosted/update-docs/" + hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testeUpdateSituationalRisk_Success(){
        UUID hostedId = UUID.randomUUID();
        SituationalRiskUpdateDTO dto = new SituationalRiskUpdateDTO(LookUp.ABORDAGEM_DE_RUA, Migrant.PASSAGEM, Homeless.REFEICAO);

        when(hostedService.updateSituationalRisk(hostedId, dto)).thenReturn(new HostedResponseUpdatedDTO("Registro atualizado"));

        ResponseEntity<HostedResponseUpdatedDTO> response = hostedController.updateHostedSituationalRisk(hostedId, dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Registro atualizado", response.getBody().message());
        verify(hostedService, times(1)).updateSituationalRisk(hostedId, dto);
    }

    @Test
    public void testeUpdateSituationalRisk_FailWrongHostedId(){
        UUID hostedId = UUID.randomUUID();
        SituationalRiskUpdateDTO dto = new SituationalRiskUpdateDTO(LookUp.ABORDAGEM_DE_RUA, Migrant.PASSAGEM, Homeless.REFEICAO);

        doThrow(NoSuchElementException.class).when(hostedService).updateSituationalRisk(hostedId, dto);
        assertThrows(NoSuchElementException.class, ()-> hostedController.updateHostedSituationalRisk(hostedId, dto));
        verify(hostedService, times(1)).updateSituationalRisk(hostedId, dto);
    }

    @Test
    public void testeUpdateSituationalRiskEndpoint_Success() throws Exception {
        UUID hostedId = UUID.randomUUID();
        SituationalRiskUpdateDTO dto = new SituationalRiskUpdateDTO(LookUp.ABORDAGEM_DE_RUA, Migrant.PASSAGEM, Homeless.REFEICAO);

        when(hostedService.updateSituationalRisk(hostedId, dto)).thenReturn(new HostedResponseUpdatedDTO("Registro atualizado"));

        String dtoJson = "{"
                + "\"lookUp\":\"ABORDAGEM_DE_RUA\","
                + "\"migrant\":\"PASSAGEM\","
                + "\"homeless\":\"REFEICAO\""
                + "}";

        mockMvc.perform(put("/hosted/update-risk/"+hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().isCreated());
        verify(hostedService, times(1)).updateSituationalRisk(hostedId, dto);
    }

    @Test
    public void testeUpdateSituationalRiskEndpoint_Fail_HostedWrongId() throws Exception {
        UUID hostedId = UUID.randomUUID();
        SituationalRiskUpdateDTO dto = new SituationalRiskUpdateDTO(LookUp.ABORDAGEM_DE_RUA, Migrant.PASSAGEM, Homeless.REFEICAO);

        doThrow(NoSuchElementException.class).when(hostedService).updateSituationalRisk(hostedId, dto);
        String dtoJson = "{"
                + "\"lookUp\":\"ABORDAGEM_DE_RUA\","
                + "\"migrant\":\"PASSAGEM\","
                + "\"homeless\":\"REFEICAO\""
                + "}";

        mockMvc.perform(put("/hosted/update-risk/"+hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().isNotFound());
        verify(hostedService, times(1)).updateSituationalRisk(hostedId, dto);
    }

    @Test
    public void testeUpdateSituationalRiskEndpoint_Fail_InvalidId() throws Exception {
        UUID hostedId = null;
        SituationalRiskUpdateDTO dto = new SituationalRiskUpdateDTO(LookUp.ABORDAGEM_DE_RUA, Migrant.PASSAGEM, Homeless.REFEICAO);

        doThrow(IllegalArgumentException.class).when(hostedService).updateSituationalRisk(hostedId, dto);
        String dtoJson = "{"
                + "\"lookUp\":\"ABORDAGEM_DE_RUA\","
                + "\"migrant\":\"PASSAGEM\","
                + "\"homeless\":\"REFEICAO\""
                + "}";

        mockMvc.perform(put("/hosted/update-risk/"+hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))
                .andExpect(status().isBadRequest());
        verify(hostedService, never()).updateSituationalRisk(hostedId, dto);
    }

    @Test
    public void testeUpdateHasFamily_Success(){
        UUID hostedId = UUID.randomUUID();
        FamilyCompositionDTO dto = new FamilyCompositionDTO(true, false);
        when(hostedService.updateHasFamily(hostedId, dto)).thenReturn(new HostedResponseUpdatedDTO("Registro atualizado"));

        ResponseEntity<HostedResponseUpdatedDTO> response = hostedController.updateHostedHasFamily(hostedId, dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Registro atualizado", response.getBody().message());
        verify(hostedService, times(1)).updateHasFamily(hostedId, dto);
    }

    @Test
    public void testeUpdateHasFamily_FailHostedIdNotExists(){
        UUID hostedId = UUID.randomUUID();
        FamilyCompositionDTO dto = new FamilyCompositionDTO(true, false);
        doThrow(NoSuchElementException.class).when(hostedService).updateHasFamily(hostedId, dto);

        assertThrows(NoSuchElementException.class, ()-> {
            hostedController.updateHostedHasFamily(hostedId, dto);
        });

        verify(hostedService, times(1)).updateHasFamily(hostedId, dto);
    }

    @Test
    public void testeUpdateHasFamily_FailHostedIdNull(){
        UUID hostedId = null;
        FamilyCompositionDTO dto = new FamilyCompositionDTO(true, false);
        when(hostedService.updateHasFamily(hostedId, dto)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, ()-> {
            hostedController.updateHostedHasFamily(hostedId, dto);
        });

        verify(hostedService, times(1)).updateHasFamily(hostedId, dto);
    }

    @Test
    public void testeUpdateHasFamily_FailDtoIsNull(){
        UUID hostedId = UUID.randomUUID();
        FamilyCompositionDTO dto = null;
        when(hostedService.updateHasFamily(hostedId, dto)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, ()-> {
            hostedController.updateHostedHasFamily(hostedId, dto);
        });

        verify(hostedService, times(1)).updateHasFamily(hostedId, dto);
    }

    @Test
    public void testUpdateHasFamilyEndpoint_Success() throws Exception {
        UUID hostedId = UUID.randomUUID();
        FamilyCompositionDTO dto = new FamilyCompositionDTO(true, false);
        when(hostedService.updateHasFamily(hostedId,dto)).thenReturn(new HostedResponseUpdatedDTO("Registro atualizado"));
        String jsonDto = "{"
                +"\"hasFamily\":true,"
                +"\"hasFamilyBond\":false"
                +"}";

        mockMvc.perform(put("/hosted/update-has-family/"+hostedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonDto))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateHasFamilyEndpoint_FailHostedIdNotExists() throws Exception {
        UUID hostedId = UUID.randomUUID();
        FamilyCompositionDTO dto = new FamilyCompositionDTO(true, false);
        when(hostedService.updateHasFamily(hostedId,dto)).thenThrow(NoSuchElementException.class);
        String jsonDto = "{"
                +"\"hasFamily\":true,"
                +"\"hasFamilyBond\":false"
                +"}";

        mockMvc.perform(put("/hosted/update-has-family/"+hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateHasFamilyEndpoint_FailHostedIdNull() throws Exception {
        UUID hostedId = null;
        FamilyCompositionDTO dto = new FamilyCompositionDTO(true, false);
        when(hostedService.updateHasFamily(hostedId,dto)).thenThrow(IllegalArgumentException.class);
        String jsonDto = "{"
                +"\"hasFamily\":true,"
                +"\"hasFamilyBond\":false"
                +"}";

        mockMvc.perform(put("/hosted/update-has-family/"+hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateHasFamilyEndpoint_FailDtoIsNull() throws Exception {
        UUID hostedId = UUID.randomUUID();
        FamilyCompositionDTO dto = null;
        when(hostedService.updateHasFamily(hostedId,dto)).thenThrow(IllegalArgumentException.class);
        String jsonDto = "null";

        mockMvc.perform(put("/hosted/update-has-family/"+hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateFamilyMember_Success() {
        UUID hostedId = UUID.randomUUID();

        List<FamilyTableMemberDTO> familyTable = List.of(
                new FamilyTableMemberDTO("Maria", 45, Gender.FEMALE, MaritalStatus.CASADO, Education.ENSINO_SUPERIOR, "Professor"),
                new FamilyTableMemberDTO("João", 50, Gender.MALE, MaritalStatus.CASADO, Education.ANALFABETO, "Engenheiro")
        );

        HostedResponseUpdatedDTO responseDto = new HostedResponseUpdatedDTO("Registro atualizado");

        // Simula o comportamento do serviço
        when(hostedService.updateFamilyTable(hostedId, familyTable)).thenReturn(responseDto);

        // Chama diretamente o método do controller
        ResponseEntity<HostedResponseUpdatedDTO> response = hostedController.updateFamilyMember(hostedId, familyTable);

        // Verifica se o status da resposta é 201 Created
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Registro atualizado", response.getBody().message());

        // Verifica se o serviço foi chamado corretamente
        verify(hostedService, times(1)).updateFamilyTable(hostedId, familyTable);
    }

    @Test
    public void testUpdateFamilyMember_HostedNotFound() {
        UUID hostedId = UUID.randomUUID();

        List<FamilyTableMemberDTO> familyTable = List.of(
                new FamilyTableMemberDTO("Maria", 45, Gender.FEMALE, MaritalStatus.CASADO, Education.POS_GRADUACAO, "Professor")
        );

        // Simula a exceção lançada pelo serviço
        when(hostedService.updateFamilyTable(hostedId, familyTable)).thenThrow(new NoSuchElementException("Acolhido não existe"));

        // Verifica se a exceção foi lançada e o ControllerAdvice capturou corretamente
        NoSuchElementException thrownException = assertThrows(NoSuchElementException.class, () -> {
            hostedController.updateFamilyMember(hostedId, familyTable);
        });

        assertEquals("Acolhido não existe", thrownException.getMessage());
        verify(hostedService, times(1)).updateFamilyTable(hostedId, familyTable);
    }

    @Test
    public void testUpdateFamilyMember_FamilyLinkNotFound() {
        UUID hostedId = UUID.randomUUID();

        List<FamilyTableMemberDTO> familyTable = List.of(
                new FamilyTableMemberDTO("João", 50,
                        Gender.MALE, MaritalStatus.CASADO,
                        Education.ENSINO_BASICO, "Carpinteiro")
        );

        // Simula a exceção lançada pelo serviço
        when(hostedService.updateFamilyTable(hostedId, familyTable)).thenThrow(new NoSuchElementException("Vínculo familiar não existe"));

        // Verifica se a exceção foi lançada e o ControllerAdvice capturou corretamente
        NoSuchElementException thrownException = assertThrows(NoSuchElementException.class, () -> {
            hostedController.updateFamilyMember(hostedId, familyTable);
        });

        assertEquals("Vínculo familiar não existe", thrownException.getMessage());
        verify(hostedService, times(1)).updateFamilyTable(hostedId, familyTable);
    }

    @Test
    public void testUpdateFamilyMemberEndpoint_Success() throws Exception {
        UUID hostedId = UUID.randomUUID();

        String jsonDto = "["
                + "{\"name\":\"Maria\", \"age\":45, \"gender\":\"FEMALE\", \"maritalStatus\":\"CASADO\", \"education\":\"ENSINO_SUPERIOR\", \"occupation\":\"Professor\"},"
                + "{\"name\":\"João\", \"age\":50, \"gender\":\"MALE\", \"maritalStatus\":\"CASADO\", \"education\":\"ENSINO_SUPERIOR\", \"occupation\":\"Engenheiro\"}"
                + "]";


        List<FamilyTableMemberDTO> familyTable = List.of(
                new FamilyTableMemberDTO("Maria", 45,
                        Gender.FEMALE, MaritalStatus.CASADO,
                        Education.ENSINO_SUPERIOR, "Professor"),
                new FamilyTableMemberDTO("João", 50,
                        Gender.MALE, MaritalStatus.CASADO,
                        Education.ENSINO_SUPERIOR, "Engenheiro")

        );
        HostedResponseUpdatedDTO responseDto = new HostedResponseUpdatedDTO("Registro atualizado");
        // Simula o comportamento do serviço
        when(hostedService.updateFamilyTable(hostedId, familyTable)).thenReturn(responseDto);

        // Faz a requisição PUT para o endpoint
        mockMvc.perform(put("/hosted/update-family-member/" + hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isCreated());
        verify(hostedService, times(1)).updateFamilyTable(hostedId, familyTable);
    }

    @Test
    public void testUpdateFamilyMemberEndpoint_HostedNotFound() throws Exception {
        UUID hostedId = UUID.randomUUID();

        String jsonDto = "["
                + "{\"name\":\"Maria\", \"age\":45, \"gender\":\"FEMALE\", \"maritalStatus\":\"CASADO\", \"education\":\"ENSINO_SUPERIOR\", \"occupation\":\"Professor\"},"
                + "{\"name\":\"João\", \"age\":50, \"gender\":\"MALE\", \"maritalStatus\":\"CASADO\", \"education\":\"ENSINO_SUPERIOR\", \"occupation\":\"Engenheiro\"}"
                + "]";


        List<FamilyTableMemberDTO> familyTable = List.of(
                new FamilyTableMemberDTO("Maria", 45,
                        Gender.FEMALE, MaritalStatus.CASADO,
                        Education.ENSINO_SUPERIOR, "Professor"),
                new FamilyTableMemberDTO("João", 50,
                        Gender.MALE, MaritalStatus.CASADO,
                        Education.ENSINO_SUPERIOR, "Engenheiro"));
        // Simula a exceção lançada pelo serviço
        when(hostedService.updateFamilyTable(hostedId, familyTable)).thenThrow(new NoSuchElementException("Acolhido não existe"));

        // Faz a requisição PUT para o endpoint
        mockMvc.perform(put("/hosted/update-family-member/" + hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isNotFound());
        verify(hostedService, times(1)).updateFamilyTable(hostedId, familyTable);
    }

    @Test
    public void testUpdateFamilyMemberEndpoint_FamilyLinkNotFound() throws Exception {
        UUID hostedId = UUID.randomUUID();

        String jsonDto = "["
                + "{\"name\":\"Maria\", \"age\":45, \"gender\":\"FEMALE\", \"maritalStatus\":\"CASADO\", \"education\":\"ENSINO_SUPERIOR\", \"occupation\":\"Professor\"},"
                + "{\"name\":\"João\", \"age\":50, \"gender\":\"MALE\", \"maritalStatus\":\"CASADO\", \"education\":\"ENSINO_SUPERIOR\", \"occupation\":\"Engenheiro\"}"
                + "]";

        List<FamilyTableMemberDTO> familyTable = List.of(
                new FamilyTableMemberDTO("Maria", 45,
                        Gender.FEMALE, MaritalStatus.CASADO,
                        Education.ENSINO_SUPERIOR, "Professor"),
                new FamilyTableMemberDTO("João", 50,
                        Gender.MALE, MaritalStatus.CASADO,
                        Education.ENSINO_SUPERIOR, "Engenheiro"));
        // Simula a exceção lançada pelo serviço
        when(hostedService.updateFamilyTable(hostedId, familyTable)).thenThrow(new NoSuchElementException("Acolhido foi marcado como sem vínculo familiar, atualize a existência de vínculo familiar"));

        // Faz a requisição PUT para o endpoint
        mockMvc.perform(put("/hosted/update-family-member/" + hostedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isNotFound());
        verify(hostedService, times(1)).updateFamilyTable(hostedId, familyTable);
    }






}
