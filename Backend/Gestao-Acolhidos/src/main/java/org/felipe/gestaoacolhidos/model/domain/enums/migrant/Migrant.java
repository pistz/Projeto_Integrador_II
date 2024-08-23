package org.felipe.gestaoacolhidos.model.domain.enums.migrant;

public enum Migrant {
    FIXAR_RESIDENCIA("FIXAR RESIDÊNCIA"),
    PASSAGEM("PASSAGEM"),
    OUTRO("OUTRO");

    private final String descricao;
    Migrant(String descricao) {
        this.descricao = descricao;
    }
}
