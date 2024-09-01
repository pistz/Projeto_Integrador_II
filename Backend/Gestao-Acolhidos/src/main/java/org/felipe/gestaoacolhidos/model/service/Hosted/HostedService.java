package org.felipe.gestaoacolhidos.model.service.Hosted;

import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.dto.Hosted.*;
import org.felipe.gestaoacolhidos.model.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.SituationalRisk.SituationalRiskUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface HostedService {

    HostedResponseCreatedDTO create(HostedCreateNewDTO hosted);
    HostedResponseUpdatedDTO updateIdentification(UUID id, HostedCreateNewDTO hosted);
    HostedResponseDeletedDTO delete(UUID id);
    Hosted findById(UUID id);
    List<Hosted> findAll();

    HostedResponseUpdatedDTO updateDocuments(UUID hostedId, DocumentsUpdateDTO documents);
    HostedResponseUpdatedDTO updateSituacionalRisk(UUID hostedId, SituationalRiskUpdateDTO dto);
    HostedResponseUpdatedDTO updateFamilyComposition(Hosted hosted);
    HostedResponseUpdatedDTO updatePoliceReport(Hosted hosted);
    HostedResponseUpdatedDTO updateReferenceAddress(Hosted hosted);
    HostedResponseUpdatedDTO updateSocialPrograms(Hosted hosted);
    HostedResponseUpdatedDTO updateMedicalRecord(Hosted hosted);
    HostedResponseUpdatedDTO updateCustomTreatments(Hosted hosted);
}
