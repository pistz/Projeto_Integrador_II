package org.felipe.gestaoacolhidos.model.domain.dto.NightReception;

import org.felipe.gestaoacolhidos.model.domain.dto.Hosted.HostedResumedDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record NightReceptionDTO(
        UUID receptionId,
        LocalDate date,
        List<HostedResumedDTO> hostedList
) {
}
