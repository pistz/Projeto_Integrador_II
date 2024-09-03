package org.felipe.gestaoacolhidos.model.dto.Hosted.SocialPrograms;

import java.math.BigDecimal;

public record SocialProgramsDTO(
        boolean hasPasseDeficiente,
        boolean hasPasseIdoso,
        boolean hasRendaCidada,
        boolean hasAcaoJovem,
        boolean hasVivaLeite,
        boolean hasBPC_LOAS,
        boolean hasBolsaFamilia,
        boolean hasPETI,
        String others,
        int howLong,
        BigDecimal wage
) {
}
