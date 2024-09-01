package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SituationalRisk;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.felipe.gestaoacolhidos.model.domain.enums.homeless.Homeless;
import org.felipe.gestaoacolhidos.model.domain.enums.lookup.LookUp;
import org.felipe.gestaoacolhidos.model.domain.enums.migrant.Migrant;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "hosted_situacao_de_risco")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SituationalRisk {

    @Id
    private UUID id;

    @Column(nullable = false, name = "procura")
    @Enumerated(EnumType.STRING)
    private LookUp lookUp;

    @Column(name = "motivo_migrante")
    @Enumerated(EnumType.STRING)
    private Migrant migrant;

    @Column(name = "populacao_rua")
    @Enumerated(EnumType.STRING)
    private Homeless homeless;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
