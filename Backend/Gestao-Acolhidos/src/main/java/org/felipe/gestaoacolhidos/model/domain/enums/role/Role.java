package org.felipe.gestaoacolhidos.model.domain.enums.role;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN("ADMIN"),
    BOARD("BOARD"),
    SECRETARY("SECRETARY");

    public String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
