package org.felipe.gestaoacolhidos.model.dto.Hosted.Documents;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentsUpdateDTO (
        String generalRegisterRG,
        LocalDate dateOfIssueRG,
        String driversLicenseNumber,
        DocumentsBirthCertificateDTO birthCertificateDTO
){
}
