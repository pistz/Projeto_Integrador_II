package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.PoliceReport;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "db_boletim_ocorrencia")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PoliceReport {

    @Id
    private UUID id;

    @Column(name = "protocolo_BO")
    private String reportProtocol;

    @Column(name = "delegacia")
    private String policeDepartment;

    @Column(name = "cidade_delegacia")
    private String city;

    @Column(name = "created_at")
    private LocalDate createdAt;
}
