package org.felipe.gestaoacolhidos.model.domain.dto.NightStand;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record NightStandCreateDTO(
        LocalDate date,
        List<UUID> hostedList
) {
}
