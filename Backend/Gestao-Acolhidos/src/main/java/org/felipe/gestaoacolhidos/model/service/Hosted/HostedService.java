package org.felipe.gestaoacolhidos.model.service.Hosted;

import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.dto.Hosted.*;
import org.felipe.gestaoacolhidos.model.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.FamilyComposition.FamilyCompositionDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.FamilyComposition.FamilyTableMemberDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.PoliceReport.PoliceReportDTO;
import org.felipe.gestaoacolhidos.model.dto.Hosted.ReferenceAddress.ReferenceAddressDTO;
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
    HostedResponseUpdatedDTO updateHasFamily(UUID hostedId, FamilyCompositionDTO dto);
    HostedResponseUpdatedDTO updateFamilyTable(UUID hostedId, List<FamilyTableMemberDTO> listDto);
    HostedResponseUpdatedDTO updatePoliceReport(UUID hostedId, PoliceReportDTO dto);
    HostedResponseUpdatedDTO updateReferenceAddress(UUID hostedId, ReferenceAddressDTO dto);
    HostedResponseUpdatedDTO updateSocialPrograms(Hosted hosted);
    HostedResponseUpdatedDTO updateMedicalRecord(Hosted hosted);
    HostedResponseUpdatedDTO updateCustomTreatments(Hosted hosted);
}
