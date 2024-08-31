package org.felipe.gestaoacolhidos.model.dto.Hosted.Documents;

import java.util.UUID;

public record DocumentsBirthCertificateDTO(
        long certificateNumber,
        String sheets,
        int book
) {
}
