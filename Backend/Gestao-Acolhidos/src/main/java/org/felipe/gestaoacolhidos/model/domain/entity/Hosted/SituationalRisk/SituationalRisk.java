package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SituationalRisk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.felipe.gestaoacolhidos.model.domain.enums.homeless.Homeless;
import org.felipe.gestaoacolhidos.model.domain.enums.lookup.LookUp;
import org.felipe.gestaoacolhidos.model.domain.enums.migrant.Migrant;

import java.util.UUID;

@Entity
@Table(name = "db_situacao_de_risco")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SituationalRisk {

    @Id
    private UUID id;

    @Column(nullable = false, name = "procura")
    private LookUp lookUp;

    @Column(nullable = false, name = "motivo_migrante")
    private Migrant migrant;

    @Column(nullable = false, name = "populacao_rua")
    private Homeless homeless;
}
