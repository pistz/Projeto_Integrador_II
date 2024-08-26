package org.felipe.gestaoacolhidos.model.service.User;

import org.felipe.gestaoacolhidos.model.domain.entity.User.User;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.felipe.gestaoacolhidos.model.dto.User.UserDeletedDTO;
import org.felipe.gestaoacolhidos.model.exceptions.UserAlreadyExistsException;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserResponseDTO;
import org.felipe.gestaoacolhidos.model.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Role role = Role.valueOf(createUserDTO.role());
        userCreated.setRoles(role);
        userRepository.save(userCreated);

        return new UserCreatedResponseDTO("User created: " + userCreated.getId());
    }

    @Override
    public UserDeletedDTO deleteUser(UUID userId) {
        if(userId == null){
            throw new IllegalArgumentException("No user selected");
        }
        userRepository.deleteById(userId);
        return new UserDeletedDTO("User deleted: " + userId);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        List<UserResponseDTO> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            UserResponseDTO userToAdd = new UserResponseDTO(user.getId(), user.getEmail(), user.getRoles());
            users.add(userToAdd);
        });
        return users;
    }

    @Override
    public UserResponseDTO findById(UUID id) {
        var exists = userRepository.findById(id);
        if(exists.isEmpty()) {
            throw new IllegalArgumentException("Not found user with id: " + id);
        }
        return new UserResponseDTO(exists.get().getId(), exists.get().getEmail(), exists.get().getRoles());
    }
}
