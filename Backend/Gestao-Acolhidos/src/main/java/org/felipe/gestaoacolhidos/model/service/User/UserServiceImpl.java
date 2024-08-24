package org.felipe.gestaoacolhidos.model.service.User;

import org.felipe.gestaoacolhidos.model.domain.entity.User.User;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.felipe.gestaoacolhidos.model.exceptions.UserAlreadyExistsException;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserResponseDTO;
import org.felipe.gestaoacolhidos.model.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserCreatedResponseDTO createUser(UserCreateDTO createUserDTO) {
        var user = userRepository.findByEmail(createUserDTO.email());
        if(user.isPresent()) {
            throw new UserAlreadyExistsException("Usuario já existe, não pode ser registrado novamente");
        }
        User userCreated = new User();
        userCreated.setEmail(createUserDTO.email());
        userCreated.setPassword(passwordEncoder.encode(createUserDTO.password()));
        Set<Role> role = new HashSet<>();
        role.add(createUserDTO.role());
        userCreated.setRoles(role);
        userRepository.save(userCreated);

        return new UserCreatedResponseDTO("User created: " + userCreated.getId());
    }

    @Override
    public void deleteUser(UUID userId) {

    }

    @Override
    public List<UserResponseDTO> findAll() {
        return List.of();
    }
}
