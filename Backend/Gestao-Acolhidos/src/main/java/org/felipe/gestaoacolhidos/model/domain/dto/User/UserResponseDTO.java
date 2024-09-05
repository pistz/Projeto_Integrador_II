package org.felipe.gestaoacolhidos.model.domain.dto.User;

import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;

import java.util.UUID;

public record UserResponseDTO(UUID id, String email, Role role) {
}
