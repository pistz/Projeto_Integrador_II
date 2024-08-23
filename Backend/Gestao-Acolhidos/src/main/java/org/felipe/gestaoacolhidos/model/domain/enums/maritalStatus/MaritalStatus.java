package org.felipe.gestaoacolhidos.model.domain.enums.maritalStatus;

public enum MaritalStatus {
    CASADO("CASADO"),
    SOLTEIRO("SOLTEIRO"),
    VIUVO("VIUVO"),
    DIVORCIADO("DIVORCIADO"),
    SEPARADO("SEPARADO"),
    UNIAO_ESTAVEL("UNIAO ESTAVEL");

    private String descricao;
    MaritalStatus(String descricao) {
        this.descricao = descricao;
    }

}
