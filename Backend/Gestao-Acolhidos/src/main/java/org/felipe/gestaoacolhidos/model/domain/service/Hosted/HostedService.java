package org.felipe.gestaoacolhidos.model.domain.service.Hosted;

import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedCreateNewDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseCreatedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseDeletedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResponseUpdatedDTO;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.CustomTreatments.CustomTreatmentsDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents.DocumentsUpdateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.FamilyComposition.FamilyCompositionDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.FamilyComposition.FamilyTableMemberDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.MedicalRecord.MedicalRecordDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.PoliceReport.PoliceReportDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.ReferenceAddress.ReferenceAddressDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.SituationalRisk.SituationalRiskUpdateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.SocialPrograms.SocialProgramsDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HostedService {

    HostedResponseCreatedDTO create(HostedCreateNewDTO hosted);
    HostedResponseUpdatedDTO updateIdentification(UUID id, HostedCreateNewDTO dto);
    HostedResponseDeletedDTO delete(UUID id);
    Hosted findById(UUID id);
    List<Hosted> findAll();
    List<LocalDate> findAllNightReceptions(UUID id);

    HostedResponseUpdatedDTO updateDocuments(UUID hostedId, DocumentsUpdateDTO documents);
    HostedResponseUpdatedDTO updateSituationalRisk(UUID hostedId, SituationalRiskUpdateDTO dto);
    HostedResponseUpdatedDTO updateHasFamily(UUID hostedId, FamilyCompositionDTO dto);
    HostedResponseUpdatedDTO updateFamilyTable(UUID hostedId, List<FamilyTableMemberDTO> listDto);
    HostedResponseUpdatedDTO updatePoliceReport(UUID hostedId, PoliceReportDTO dto);
    HostedResponseUpdatedDTO updateReferenceAddress(UUID hostedId, ReferenceAddressDTO dto);
    HostedResponseUpdatedDTO updateSocialPrograms(UUID hostedId, SocialProgramsDTO dto);
    HostedResponseUpdatedDTO updateMedicalRecord(UUID hostedId, MedicalRecordDTO dto);
    HostedResponseUpdatedDTO updateCustomTreatments(UUID hostedId, CustomTreatmentsDTO dto);
}
