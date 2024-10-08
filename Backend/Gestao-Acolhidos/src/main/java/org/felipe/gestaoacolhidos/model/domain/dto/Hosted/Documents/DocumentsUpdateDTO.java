package org.felipe.gestaoacolhidos.model.domain.dto.Hosted.Documents;

import java.time.LocalDate;

public record DocumentsUpdateDTO (
        String generalRegisterRG,
        LocalDate dateOfIssueRG,
        String driversLicenseNumber,
        DocumentsBirthCertificateDTO birthCertificate
){
}
