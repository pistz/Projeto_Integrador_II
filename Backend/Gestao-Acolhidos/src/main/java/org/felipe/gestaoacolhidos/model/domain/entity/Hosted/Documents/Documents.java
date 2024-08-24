package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Documents;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.BirthCertificate.BirthCertificate;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.PoliceReport.PoliceReport;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "hosted_documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "identidate_rg")
    private String generalRegisterRG;

    @Column(name = "data_emissao_rg")
    private LocalDate dateOfIssueRG;

    @Column(nullable = false, unique = true, name = "CPF")
    private String socialSecurityNumber;

    @Column(nullable = false, name = "possui_carteira_CNH")
    private boolean hasLicense;

    @Column(name = "CNH")
    private String driversLicenseNumber;

    @OneToOne(cascade = CascadeType.ALL)
    private BirthCertificate birthCertificate;

}
