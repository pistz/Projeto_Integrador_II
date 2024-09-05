package org.felipe.gestaoacolhidos.model.domain.service.User;

import org.felipe.gestaoacolhidos.model.domain.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserDeletedDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserCreatedResponseDTO createUser(UserCreateDTO createUserDTO);
    UserDeletedDTO deleteUser(UUID userId);
    List<UserResponseDTO> findAll();
    UserResponseDTO findById(UUID id);
}
