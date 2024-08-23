package org.felipe.gestaoacolhidos.model.domain.enums.education;

public enum Education {
    ANALFABETO("ANALFABETO"),
    ENSINO_BASICO("ENSINO BASICO"),
    ENSINO_MEDIO("ENSINO MEDIO"),
    ENSINO_SUPERIOR("ENSINO SUPERIOR"),
    POS_GRADUACAO("POS GRADUAÇÃO");

    private String descricao;
    Education(String descricao) {
        this.descricao = descricao;
    }
}
