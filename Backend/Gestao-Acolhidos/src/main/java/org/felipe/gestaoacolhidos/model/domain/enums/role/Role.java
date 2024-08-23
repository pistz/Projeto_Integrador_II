package org.felipe.gestaoacolhidos.model.domain.enums.role;

public enum Role {
    ADMIN("ADMIN"),
    BOARD("BOARD"),
    SECRETARY("SECRETARY");

    public String role;

    Role(String role) {
        this.role = role;
    }
}
