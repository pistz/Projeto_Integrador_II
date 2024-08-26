package org.felipe.gestaoacolhidos.model.dto.Hosted;

import java.time.LocalDate;

public record HostedCreateNewDTO(
        String firstName,
        String lastName,
        String socialSecurityNumber,
        LocalDate birthDay,
        long paperTrail,
        String fathersName,
        String mothersName,
        String occupation,
        String cityOrigin,
        String stateOrigin,
        LocalDate createdAt
) {
}
