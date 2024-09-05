package org.felipe.gestaoacolhidos.model.domain.dto.Hosted.SituationalRisk;

import org.felipe.gestaoacolhidos.model.domain.enums.homeless.Homeless;
import org.felipe.gestaoacolhidos.model.domain.enums.lookup.LookUp;
import org.felipe.gestaoacolhidos.model.domain.enums.migrant.Migrant;

public record SituationalRiskUpdateDTO(LookUp lookUp, Migrant migrant, Homeless homeless) {
}
