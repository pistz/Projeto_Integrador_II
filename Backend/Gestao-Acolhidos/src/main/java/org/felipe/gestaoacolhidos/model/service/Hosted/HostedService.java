package org.felipe.gestaoacolhidos.model.service.Hosted;

import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.dto.Hosted.*;

import java.util.List;
import java.util.UUID;

public interface HostedService {

    HostedResponseCreatedDTO create(HostedCreateNewDTO hosted);
    HostedResponseUpdatedDTO update(Hosted hosted);
    HostedResponseDeletedDTO delete(UUID id);
    Hosted findById(UUID id);
    Hosted findByCPF(String cpf);
    Hosted findByFirstNameAndLastName(HostedFullNameDTO hostedFullNameDTO);
    Hosted findByPaperTrail(long paperTrail);
    List<Hosted> findAll();
}
