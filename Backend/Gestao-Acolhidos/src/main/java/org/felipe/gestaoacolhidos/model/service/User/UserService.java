package org.felipe.gestaoacolhidos.model.service.User;

import org.felipe.gestaoacolhidos.model.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserDeletedDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserCreatedResponseDTO createUser(UserCreateDTO createUserDTO);
    UserDeletedDTO deleteUser(UUID userId);
    List<UserResponseDTO> findAll();
    UserResponseDTO findById(UUID id);

}
