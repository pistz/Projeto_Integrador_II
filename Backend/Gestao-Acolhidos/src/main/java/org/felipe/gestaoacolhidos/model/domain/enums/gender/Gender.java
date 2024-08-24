package org.felipe.gestaoacolhidos.model.domain.enums.gender;

public enum Gender {
    MALE("HOMEM"),
    FEMALE("MULHER"),
    OTHER("OUTRO");
    private final String value;
    Gender(String value) {
        this.value = value;
    }
}

