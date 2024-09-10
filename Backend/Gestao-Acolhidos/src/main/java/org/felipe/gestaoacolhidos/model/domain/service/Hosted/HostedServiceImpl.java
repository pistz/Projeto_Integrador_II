package org.felipe.gestaoacolhidos.model.domain.service.Hosted;

import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedCreateNewDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseCreatedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseDeletedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseUpdatedDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.BirthCertificate.BirthCertificate;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.CustomTreatments.CustomTreatments;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Documents.Documents;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyComposition.FamilyComposition;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyTable.FamilyTable;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.MedicalRecord.MedicalRecord;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.PoliceReport.PoliceReport;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.ReferenceAddress.ReferenceAddress;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SituationalRisk.SituationalRisk;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SocialPrograms.SocialPrograms;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.CustomTreatments.CustomTreatmentsDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.FamilyComposition.FamilyCompositionDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.FamilyComposition.FamilyTableMemberDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.MedicalRecord.MedicalRecordDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.PoliceReport.PoliceReportDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.ReferenceAddress.ReferenceAddressDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.SituationalRisk.SituationalRiskUpdateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.SocialPrograms.SocialProgramsDTO;
import org.felipe.gestaoacolhidos.model.exceptions.HostedAlreadyRegisteredException;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.logs.UserLoggingInterceptor;
import org.felipe.gestaoacolhidos.model.repository.hosted.HostedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class HostedServiceImpl implements HostedService {

    @Autowired
    private HostedRepository hostedRepository;

    @Autowired
    private UserLoggingInterceptor interceptor;

    @Override
    @Transactional
    public HostedResponseCreatedDTO create(HostedCreateNewDTO hosted) {
        validateCPF(hosted.socialSecurityNumber());
        var exists = hostedRepository.existsBySocialSecurityNumber(hosted.socialSecurityNumber());
        if(exists) {
            throw new HostedAlreadyRegisteredException("Acolhido já possui registro ativo");
        }

        Hosted registerHosted = Hosted.builder()
                .firstName(hosted.firstName())
                .lastName(hosted.lastName())
                .socialSecurityNumber(hosted.socialSecurityNumber())
                .birthDay(hosted.birthDay())
                .paperTrail(hosted.paperTrail())
                .fathersName(hosted.fathersName())
                .mothersName(hosted.mothersName())
                .occupation(hosted.occupation())
                .cityOrigin(hosted.cityOrigin())
                .stateOrigin(hosted.stateOrigin())
                .createdAt(LocalDate.now())
                .build();
        registerHosted.setAge(registerHosted.getAge());
        hostedRepository.save(registerHosted);
        return new HostedResponseCreatedDTO("Acolhido registrado");
    }

    @Override
    @Transactional
    public HostedResponseDeletedDTO delete(UUID id) {
        var exists = hostedRepository.existsById(id);
        if(exists) {
            hostedRepository.deleteById(id);
            return new HostedResponseDeletedDTO("Registro deletado");
        }
        throw new NoSuchElementException("Id não existe");
    }

    @Override
    public Hosted findById(UUID id) {
        return hostedRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Hosted> findAll() {
        return hostedRepository.findAll();
    }

    @Override
    public List<LocalDate> findAllNightReceptions(UUID id) {
        return hostedRepository.queryHostedByNighReception(id);
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updateIdentification(UUID id, HostedCreateNewDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(id);
        Hosted updateHost = registeredHosted.get();
        validateCPF(dto.socialSecurityNumber());

        updateHost.setSocialSecurityNumber(dto.socialSecurityNumber());

        updateHost.setFirstName(dto.firstName());
        updateHost.setLastName(dto.lastName());
        updateHost.setPaperTrail(dto.paperTrail());
        updateHost.setBirthDay(dto.birthDay());
        updateHost.setMothersName(dto.mothersName());
        updateHost.setFathersName(dto.fathersName());
        updateHost.setOccupation(dto.occupation());
        updateHost.setCityOrigin(dto.cityOrigin());
        updateHost.setStateOrigin(dto.stateOrigin());

        updateHost.setUpdatedAt(LocalDate.now());
        updateHost.setUpdatedBy(interceptor.getRegisteredUser());

        hostedRepository.save(updateHost);

        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updateDocuments(UUID hostedId, DocumentsUpdateDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedDocuments = registeredHosted.get();
        if(checkAllNullProperties(dto)){
            return new HostedResponseUpdatedDTO("Registro vazio");
        }
        Documents documents;
        if(updateHostedDocuments.getOtherDocuments() == null){
            documents = new Documents();
            documents.setId(UUID.randomUUID());
            updateHostedDocuments.setOtherDocuments(documents);
        }
        documents = updateHostedDocuments.getOtherDocuments();

        documents.setGeneralRegisterRG(dto.generalRegisterRG());
        documents.setDateOfIssueRG(dto.dateOfIssueRG());
        if(dto.driversLicenseNumber() != null){
            documents.setHasLicense(true);
            documents.setDriversLicenseNumber(dto.driversLicenseNumber());
        }

        if(documents.getBirthCertificate() == null){
            BirthCertificate birthCertificate = new BirthCertificate();
            birthCertificate.setId(UUID.randomUUID());
            documents.setBirthCertificate(birthCertificate);
        }
        documents.getBirthCertificate().setCertificateNumber(dto.birthCertificate().certificateNumber());
        documents.getBirthCertificate().setSheets(dto.birthCertificate().sheets());
        documents.getBirthCertificate().setBook(dto.birthCertificate().book());
        documents.setUpdatedAt(LocalDate.now());

        updateHostedDocuments.setOtherDocuments(documents);
        updateHostedDocuments.setUpdatedBy(interceptor.getRegisteredUser());
        updateHostedDocuments.setUpdatedAt(LocalDate.now());

        hostedRepository.save(updateHostedDocuments);
        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updateSituationalRisk(UUID hostedId, SituationalRiskUpdateDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedSituacionalRisk = registeredHosted.get();
        if(checkAllNullProperties(dto)){
            return new HostedResponseUpdatedDTO("Registro vazio");
        }
        SituationalRisk situationalRisk;
        if(updateHostedSituacionalRisk.getSituationalRisk() == null){
            situationalRisk = new SituationalRisk();
            situationalRisk.setId(UUID.randomUUID());
            updateHostedSituacionalRisk.setSituationalRisk(situationalRisk);
        }
        situationalRisk = updateHostedSituacionalRisk.getSituationalRisk();

        situationalRisk.setLookUp(dto.lookUp());
        situationalRisk.setMigrant(dto.migrant());
        situationalRisk.setHomeless(dto.homeless());
        situationalRisk.setUpdatedAt(LocalDate.now());

        updateHostedSituacionalRisk.setSituationalRisk(situationalRisk);
        updateHostedSituacionalRisk.setUpdatedBy(interceptor.getRegisteredUser());
        updateHostedSituacionalRisk.setUpdatedAt(LocalDate.now());

        hostedRepository.save(updateHostedSituacionalRisk);

        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updateHasFamily(UUID hostedId, FamilyCompositionDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedFamilyComposition = registeredHosted.get();
        if(checkAllNullProperties(dto)){
            return new HostedResponseUpdatedDTO("Registro vazio");
        }
        FamilyComposition familyComposition;
        if(updateHostedFamilyComposition.getFamilyComposition() == null){
            familyComposition = new FamilyComposition();
            familyComposition.setId(UUID.randomUUID());
            updateHostedFamilyComposition.setFamilyComposition(familyComposition);
        }
        familyComposition = updateHostedFamilyComposition.getFamilyComposition();

        familyComposition.setHasFamily(dto.hasFamily());
        familyComposition.setHasFamilyBond(dto.hasFamilyBond());
        familyComposition.setUpdatedAt(LocalDate.now());

        updateHostedFamilyComposition.setFamilyComposition(familyComposition);
        updateHostedFamilyComposition.setUpdatedBy(interceptor.getRegisteredUser());
        updateHostedFamilyComposition.setUpdatedAt(LocalDate.now());
        hostedRepository.save(updateHostedFamilyComposition);
        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updateFamilyTable(UUID hostedId, List<FamilyTableMemberDTO> memberListDto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedFamilyTable = registeredHosted.get();
        if(memberListDto.isEmpty()){
            return new HostedResponseUpdatedDTO("Registro vazio");
        }
        if(updateHostedFamilyTable.getFamilyComposition() == null ||
            !updateHostedFamilyTable.getFamilyComposition().isHasFamily() ||
                !updateHostedFamilyTable.getFamilyComposition().isHasFamilyBond()
        ){
            throw new NoSuchElementException("Acolhido foi marcado como sem vínculo familiar, atualize a existência de vínculo familiar");
        }
        List<FamilyTable> familyTableList;
        if(updateHostedFamilyTable.getFamilyTable() == null){
            familyTableList = new ArrayList<>();
            updateHostedFamilyTable.setFamilyTable(familyTableList);
        }
        familyTableList = updateHostedFamilyTable.getFamilyTable();


        for(FamilyTableMemberDTO member : memberListDto){
            familyTableList.add(createFamilyTableMember(member));
        }
        updateHostedFamilyTable.setFamilyTable(familyTableList);
        updateHostedFamilyTable.setUpdatedBy(interceptor.getRegisteredUser());
        updateHostedFamilyTable.setUpdatedAt(LocalDate.now());
        hostedRepository.save(updateHostedFamilyTable);
        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updatePoliceReport(UUID hostedId, PoliceReportDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedPoliceReport = registeredHosted.get();
        if(checkAllNullProperties(dto)){
            return new HostedResponseUpdatedDTO("Registro vazio");
        }
        List<PoliceReport> policeReportList;
        if(updateHostedPoliceReport.getPoliceReport() == null){
            policeReportList = new ArrayList<>();
            updateHostedPoliceReport.setPoliceReport(policeReportList);
        }
        policeReportList = updateHostedPoliceReport.getPoliceReport();

        PoliceReport updatePoliceReport = new PoliceReport();
        updatePoliceReport.setId(UUID.randomUUID());
        updatePoliceReport.setPoliceDepartment(dto.policeDepartment());
        updatePoliceReport.setReportProtocol(dto.reportProtocol());
        updatePoliceReport.setCity(dto.city());
        updatePoliceReport.setReportInfo(dto.reportInfo());
        updatePoliceReport.setCreatedAt(LocalDate.now());

        policeReportList.add(updatePoliceReport);

        updateHostedPoliceReport.setPoliceReport(policeReportList);
        updateHostedPoliceReport.setUpdatedAt(LocalDate.now());
        updateHostedPoliceReport.setUpdatedBy(interceptor.getRegisteredUser());

        hostedRepository.save(updateHostedPoliceReport);
        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updateReferenceAddress(UUID hostedId, ReferenceAddressDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updatedHostedReferenceAddress = registeredHosted.get();
        if(checkAllNullProperties(dto)){
            return new HostedResponseUpdatedDTO("Registro vazio");
        }
        ReferenceAddress updateReferenceAddress;
        if(updatedHostedReferenceAddress.getReferenceAddress() == null){
            updateReferenceAddress = new ReferenceAddress();
            updateReferenceAddress.setId(UUID.randomUUID());
            updatedHostedReferenceAddress.setReferenceAddress(updateReferenceAddress);
        }
        updateReferenceAddress = updatedHostedReferenceAddress.getReferenceAddress();

        updateReferenceAddress.setStreet(dto.street());
        updateReferenceAddress.setNeighborhood(dto.neighborhood());
        updateReferenceAddress.setNumber(dto.number());
        updateReferenceAddress.setCidade(dto.city());
        updateReferenceAddress.setCep(dto.cep());
        updateReferenceAddress.setPhoneNumber(dto.phoneNumber());
        updateReferenceAddress.setUpdatedAt(LocalDate.now());

        updatedHostedReferenceAddress.setReferenceAddress(updateReferenceAddress);
        updatedHostedReferenceAddress.setUpdatedAt(LocalDate.now());
        updatedHostedReferenceAddress.setUpdatedBy(interceptor.getRegisteredUser());

        hostedRepository.save(updatedHostedReferenceAddress);
        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updateSocialPrograms(UUID hostedId, SocialProgramsDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedSocialPrograms = registeredHosted.get();
        if(checkAllNullProperties(dto)){
            return new HostedResponseUpdatedDTO("Registro vazio");
        }
        SocialPrograms socialPrograms;
        if(updateHostedSocialPrograms.getSocialPrograms() == null){
            socialPrograms = new SocialPrograms();
            socialPrograms.setId(UUID.randomUUID());
            updateHostedSocialPrograms.setSocialPrograms(socialPrograms);
        }
        socialPrograms = updateHostedSocialPrograms.getSocialPrograms();

        socialPrograms.setHasPasseDeficiente(dto.hasPasseDeficiente());
        socialPrograms.setHasPasseIdoso(dto.hasPasseIdoso());
        socialPrograms.setHasRendaCidada(dto.hasRendaCidada());
        socialPrograms.setHasAcaoJovem(dto.hasAcaoJovem());
        socialPrograms.setHasVivaLeite(dto.hasVivaLeite());
        socialPrograms.setHasBPC_LOAS(dto.hasBPC_LOAS());
        socialPrograms.setHasBolsaFamilia(dto.hasBolsaFamilia());
        socialPrograms.setHasPETI(dto.hasPETI());

        socialPrograms.setOthers(dto.others());
        socialPrograms.setHowLong(dto.howLong());
        socialPrograms.setWage(dto.wage());
        socialPrograms.setUpdatedAt(LocalDate.now());

        updateHostedSocialPrograms.setSocialPrograms(socialPrograms);
        updateHostedSocialPrograms.setUpdatedAt(LocalDate.now());
        updateHostedSocialPrograms.setUpdatedBy(interceptor.getRegisteredUser());
        hostedRepository.save(updateHostedSocialPrograms);

        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updateMedicalRecord(UUID hostedId, MedicalRecordDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedMedicalRecord = registeredHosted.get();
        if(checkAllNullProperties(dto)){
            return new HostedResponseUpdatedDTO("Registro vazio");
        }
        List<MedicalRecord> medicalRecords;
        if(updateHostedMedicalRecord.getMedicalRecord() == null){
            medicalRecords = new ArrayList<>();
            updateHostedMedicalRecord.setMedicalRecord(medicalRecords);
        }
        medicalRecords = updateHostedMedicalRecord.getMedicalRecord();
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(UUID.randomUUID());
        medicalRecord.setComplaints(dto.complaints());
        medicalRecord.setCreatedAt(LocalDate.now());
        medicalRecords.add(medicalRecord);

        updateHostedMedicalRecord.setMedicalRecord(medicalRecords);
        updateHostedMedicalRecord.setUpdatedAt(LocalDate.now());
        updateHostedMedicalRecord.setUpdatedBy(interceptor.getRegisteredUser());

        hostedRepository.save(updateHostedMedicalRecord);
        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    @Transactional
    public HostedResponseUpdatedDTO updateCustomTreatments(UUID hostedId, CustomTreatmentsDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedCustomProcedures = registeredHosted.get();
        if(checkAllNullProperties(dto)){
            return new HostedResponseUpdatedDTO("Registro vazio");
        }
        List<CustomTreatments> customTreatments;
        if(updateHostedCustomProcedures.getCustomTreatments() == null){
            customTreatments = new ArrayList<>();
            updateHostedCustomProcedures.setCustomTreatments(customTreatments);
        }
        customTreatments = updateHostedCustomProcedures.getCustomTreatments();
        CustomTreatments customTreatment = new CustomTreatments();
        customTreatment.setId(UUID.randomUUID());
        customTreatment.setCreatedAt(LocalDate.now());
        customTreatment.setProcedure(dto.procedure());

        customTreatments.add(customTreatment);

        updateHostedCustomProcedures.setCustomTreatments(customTreatments);
        updateHostedCustomProcedures.setUpdatedAt(LocalDate.now());
        updateHostedCustomProcedures.setUpdatedBy(interceptor.getRegisteredUser());

        hostedRepository.save(updateHostedCustomProcedures);
        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    private void validateCPF(String cpf) {
        Pattern pattern = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
        if(!pattern.matcher(cpf).matches()){
            throw new IllegalArgumentException("CPF inválido");
        }
    }

    private Optional<Hosted> checkHostedExistence(UUID id){
        Optional<Hosted> registeredHosted = hostedRepository.findById(id);
        if(registeredHosted.isEmpty()) {
            throw new NoSuchElementException("Acolhido não existe");
        }
        return registeredHosted;
    }

    private FamilyTable createFamilyTableMember(FamilyTableMemberDTO dto) {
        FamilyTable familyTable = new FamilyTable();
        familyTable.setId(UUID.randomUUID());
        familyTable.setName(dto.name());
        familyTable.setAge(dto.age());
        familyTable.setGender(dto.gender());
        familyTable.setMaritalStatus(dto.maritalStatus());
        familyTable.setOccupation(dto.occupation());
        familyTable.setEducation(dto.education());
        familyTable.setUpdatedAt(LocalDate.now());
        return familyTable;
    }

    private boolean checkAllNullProperties(Object object){
        if (object == null) {
            return true;
        }
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                 if (field.get(object) != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar o campo: " + field.getName(), e);
            }
        }
        return true;
    }

}
