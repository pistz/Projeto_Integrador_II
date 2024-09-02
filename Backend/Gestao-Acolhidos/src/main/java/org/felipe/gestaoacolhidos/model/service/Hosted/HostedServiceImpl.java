package org.felipe.gestaoacolhidos.model.service.Hosted;

import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.BirthCertificate.BirthCertificate;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Documents.Documents;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyComposition.FamilyComposition;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyTable.FamilyTable;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SituationalRisk.SituationalRisk;
import org.felipe.gestaoacolhidos.model.dto.Hosted.*;
import org.felipe.gestaoacolhidos.model.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.FamilyComposition.FamilyCompositionDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.FamilyComposition.FamilyTableMemberDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.SituationalRisk.SituationalRiskUpdateDTO;
import org.felipe.gestaoacolhidos.model.exceptions.HostedAlreadyRegisteredException;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.logs.UserLoggingInterceptor;
import org.felipe.gestaoacolhidos.model.repository.hosted.HostedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class HostedServiceImpl implements HostedService {

    @Autowired
    private HostedRepository hostedRepository;

    @Autowired
    private UserLoggingInterceptor interceptor;

    @Override
    public HostedResponseCreatedDTO create(HostedCreateNewDTO hosted) {
        if(!validateCPF(hosted.socialSecurityNumber())){
            throw new IllegalArgumentException("CPF invalido");
        }
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
        hostedRepository.save(registerHosted);
        return new HostedResponseCreatedDTO("Acolhido registrado");
    }

    @Override
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
    public HostedResponseUpdatedDTO updateIdentification(UUID id, HostedCreateNewDTO hosted) {
        Optional<Hosted> registeredHosted = checkHostedExistence(id);
        Hosted updateHost = registeredHosted.get();
        if(validateCPF(hosted.socialSecurityNumber())){
            updateHost.setSocialSecurityNumber(hosted.socialSecurityNumber());
        }
        updateHost.setFirstName(hosted.firstName());
        updateHost.setLastName(hosted.lastName());
        updateHost.setPaperTrail(hosted.paperTrail());
        updateHost.setBirthDay(hosted.birthDay());
        updateHost.setMothersName(hosted.mothersName());
        updateHost.setFathersName(hosted.fathersName());
        updateHost.setOccupation(hosted.occupation());
        updateHost.setCityOrigin(hosted.cityOrigin());
        updateHost.setStateOrigin(hosted.stateOrigin());

        updateHost.setUpdatedAt(LocalDate.now());
        updateHost.setUpdatedBy(interceptor.getRegisteredUser());

        hostedRepository.save(updateHost);

        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    public HostedResponseUpdatedDTO updateDocuments(UUID hostedId, DocumentsUpdateDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);

        Hosted updateHostedDocuments = registeredHosted.get();
        Documents documents;
        if(updateHostedDocuments.getOtherDocuments() == null){
            documents = new Documents();
            documents.setId(UUID.randomUUID());
            updateHostedDocuments.setOtherDocuments(documents);
        }else{
            documents = updateHostedDocuments.getOtherDocuments();
        }
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
    public HostedResponseUpdatedDTO updateSituacionalRisk(UUID hostedId, SituationalRiskUpdateDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);

        Hosted updateHostedSituacionalRisk = registeredHosted.get();
        SituationalRisk situationalRisk;
        if(updateHostedSituacionalRisk.getSituationalRisk() == null){
            situationalRisk = new SituationalRisk();
            situationalRisk.setId(UUID.randomUUID());
            updateHostedSituacionalRisk.setSituationalRisk(situationalRisk);
        }else{
            situationalRisk = updateHostedSituacionalRisk.getSituationalRisk();
        }
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
    public HostedResponseUpdatedDTO updateHasFamily(UUID hostedId, FamilyCompositionDTO dto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedFamilyComposition = registeredHosted.get();
        FamilyComposition familyComposition;
        if(updateHostedFamilyComposition.getFamilyComposition() == null){
            familyComposition = new FamilyComposition();
            familyComposition.setId(UUID.randomUUID());
            updateHostedFamilyComposition.setFamilyComposition(familyComposition);
        }else{
            familyComposition = updateHostedFamilyComposition.getFamilyComposition();
        }
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
    public HostedResponseUpdatedDTO updateFamilyTable(UUID hostedId, List<FamilyTableMemberDTO> memberListDto) {
        Optional<Hosted> registeredHosted = checkHostedExistence(hostedId);
        Hosted updateHostedFamilyTable = registeredHosted.get();
        if(updateHostedFamilyTable.getFamilyComposition() == null &&
            !updateHostedFamilyTable.getFamilyComposition().isHasFamily() &&
                !updateHostedFamilyTable.getFamilyComposition().isHasFamilyBond()
        ){
            throw new NoSuchElementException("Acolhido foi marcado como sem vínculo familiar, atualize a existência de vínculo familiar");
        }
        List<FamilyTable> familyTableList;
        if(updateHostedFamilyTable.getFamilyTable() == null){
            familyTableList = new ArrayList<>();
            updateHostedFamilyTable.setFamilyTable(familyTableList);
        }else{
            familyTableList = updateHostedFamilyTable.getFamilyTable();
        }

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
    public HostedResponseUpdatedDTO updatePoliceReport(Hosted hosted) {
        //TODO - boletim de ocorrencia
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateReferenceAddress(Hosted hosted) {
        //TODO - endereço de referencia
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateSocialPrograms(Hosted hosted) {
        //TODO - programas sociais
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateMedicalRecord(Hosted hosted) {
        //TODO - boletim de saúde
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateCustomTreatments(Hosted hosted) {
        //TODO - plano personalizado de tratamentos
        return null;
    }

    private boolean validateCPF(String cpf) {
        Pattern pattern = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
        return pattern.matcher(cpf).matches();
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

}
