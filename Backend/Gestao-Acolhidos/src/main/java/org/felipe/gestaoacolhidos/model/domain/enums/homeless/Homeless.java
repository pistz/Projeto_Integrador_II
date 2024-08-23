package org.felipe.gestaoacolhidos.model.domain.enums.homeless;

public enum Homeless {
    PERNOITE("PERNOITE"),
    REFEICAO("REFEIÇÃO"),
    OUTRO("OUTRO");

    private String option;
    Homeless(String value) {
        this.option = value;
    }
}
