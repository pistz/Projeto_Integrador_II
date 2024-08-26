package org.felipe.gestaoacolhidos.model.service.Hosted;

import org.felipe.gestaoacolhidos.model.dto.Hosted.*;
import org.felipe.gestaoacolhidos.model.exceptions.HostedAlreadyRegisteredException;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.repository.hosted.HostedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class HostedServiceImpl implements HostedService {

    @Autowired
    private HostedRepository hostedRepository;

    @Override
    public HostedResponseCreatedDTO create(HostedCreateNewDTO hosted) {
        var exists = hostedRepository.findBySocialSecurityNumber(hosted.socialSecurityNumber());
        if(exists!=null) {
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
    public HostedResponseUpdatedDTO update(Hosted hosted) {
        return null;
    }

    @Override
    public HostedResponseDeletedDTO delete(UUID id) {
        return null;
    }

    @Override
    public Hosted findById(UUID id) {
        return null;
    }

    @Override
    public Hosted findByCPF(String cpf) {
        return null;
    }

    @Override
    public Hosted findByFirstNameAndLastName(HostedFullNameDTO dto) {
        return null;
    }

    @Override
    public Hosted findByPaperTrail(long paperTrail) {
        return null;
    }

    @Override
    public List<Hosted> findAll() {
        return List.of();
    }
}
