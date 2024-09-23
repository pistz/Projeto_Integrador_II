package org.felipe.gestaoacolhidos.model.domain.service.User;

import org.felipe.gestaoacolhidos.model.domain.entity.User.User;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserDeletedDTO;
import org.felipe.gestaoacolhidos.model.exceptions.UserAlreadyExistsException;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserResponseDTO;
import org.felipe.gestaoacolhidos.model.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserCreatedResponseDTO createUser(UserCreateDTO createUserDTO) {
        if(!validateEmail(createUserDTO.email())) {
            throw new IllegalArgumentException("E-mail inválido");
        }
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
    @Transactional
    public UserDeletedDTO deleteUser(UUID userId) {
        if(userId == null){
            throw new IllegalArgumentException("No user selected");
        }
        var exists = userRepository.existsById(userId);
        if(!exists){
            throw new NoSuchElementException("User not found");
        }
        userRepository.deleteById(userId);
        return new UserDeletedDTO("User deleted: " + userId);
    }

    @Override
    @Transactional
    public List<UserResponseDTO> findAll() {
        List<UserResponseDTO> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            UserResponseDTO userToAdd = new UserResponseDTO(user.getId(), user.getEmail(), user.getRoles());
            users.add(userToAdd);
        });
        return users;
    }

    @Override
    @Transactional
    public UserResponseDTO findById(UUID id) {
        var exists = userRepository.findById(id);
        if(exists.isEmpty()) {
            throw new NoSuchElementException("Not found user with id: " + id);
        }
        User found = exists.get();
        return new UserResponseDTO(found.getId(), found.getEmail(), found.getRoles());
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        return pattern.matcher(email).matches();
    }
}
