package org.felipe.gestaoacolhidos.model.dto.User;

import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;

public record UserCreateDTO(String email, String password, Role role) {
}
