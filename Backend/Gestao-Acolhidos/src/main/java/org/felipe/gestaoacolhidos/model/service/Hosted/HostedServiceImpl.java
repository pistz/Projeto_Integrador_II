package org.felipe.gestaoacolhidos.model.service.Hosted;

import org.felipe.gestaoacolhidos.model.dto.Hosted.*;
import org.felipe.gestaoacolhidos.model.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.exceptions.HostedAlreadyRegisteredException;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.repository.hosted.HostedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class HostedServiceImpl implements HostedService {

    @Autowired
    private HostedRepository hostedRepository;

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
        var registeredHosted = hostedRepository.findById(id);
        if(registeredHosted.isEmpty()) {
            throw new NoSuchElementException("Acolhido não existe");
        }

        Hosted updateHost = registeredHosted.get();

        updateHost.setFirstName(hosted.firstName());
        updateHost.setLastName(hosted.lastName());
        updateHost.setSocialSecurityNumber(hosted.socialSecurityNumber());
        updateHost.setPaperTrail(hosted.paperTrail());
        updateHost.setBirthDay(hosted.birthDay());
        updateHost.setMothersName(hosted.mothersName());
        updateHost.setFathersName(hosted.fathersName());
        updateHost.setOccupation(hosted.occupation());
        updateHost.setCityOrigin(hosted.cityOrigin());
        updateHost.setStateOrigin(hosted.stateOrigin());

        hostedRepository.save(updateHost);

        return new HostedResponseUpdatedDTO("Registro atualizado");
    }

    @Override
    public HostedResponseUpdatedDTO updateDocuments(UUID hostedId, DocumentsUpdateDTO dto) {
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateSocialRisk(Hosted hosted) {
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateFamilyComposition(Hosted hosted) {
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updatePoliceReport(Hosted hosted) {
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateReferenceAddress(Hosted hosted) {
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateSocialPrograms(Hosted hosted) {
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateMedicalRecord(Hosted hosted) {
        return null;
    }

    @Override
    public HostedResponseUpdatedDTO updateCustomTreatments(Hosted hosted) {
        return null;
    }

    private boolean validateCPF(String cpf) {
        Pattern pattern = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
        return pattern.matcher(cpf).matches();
    }
}
