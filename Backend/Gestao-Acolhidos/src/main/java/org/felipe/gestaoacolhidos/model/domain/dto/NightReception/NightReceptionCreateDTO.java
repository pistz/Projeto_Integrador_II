package org.felipe.gestaoacolhidos.model.domain.dto.NightReception;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record NightReceptionCreateDTO(
        LocalDate date,
        List<UUID> hostedList
) {
}
