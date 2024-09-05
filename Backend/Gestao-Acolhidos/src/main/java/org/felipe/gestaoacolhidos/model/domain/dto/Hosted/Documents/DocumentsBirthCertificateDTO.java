package org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents;

public record DocumentsBirthCertificateDTO(
        long certificateNumber,
        String sheets,
        int book
) {
}
