package org.felipe.gestaoacolhidos.model.domain.enums.lookup;

public enum LookUp {
    FORMAL("FORMAL COM ENCAMINHAMENTO"),
    ESPONTANEA("ESPONTANEA"),
    ABORDAGEM_DE_RUA("ABORDAGEM DE RUA");

    private final String option;
    LookUp(String opt) {
        this.option = opt;
    }
}
