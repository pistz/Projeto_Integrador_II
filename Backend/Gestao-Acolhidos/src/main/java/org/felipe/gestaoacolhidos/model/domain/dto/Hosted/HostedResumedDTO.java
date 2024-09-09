package org.felipe.gestaoacolhidos.model.domain.dto.Hosted;

import java.util.UUID;

public record HostedResumedDTO(
        UUID hostedId,
        String firstName,
        String lastName,
        String socialSecutityCPF
) {
}
