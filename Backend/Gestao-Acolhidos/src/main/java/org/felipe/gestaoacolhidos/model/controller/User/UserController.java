package org.felipe.gestaoacolhidos.model.controller.User;

import org.felipe.gestaoacolhidos.model.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserCreatedResponseDTO> createUser(@RequestBody UserCreateDTO dto){
        UserCreatedResponseDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.status(200).body("usuário: " + id + " deletado com sucesso!");
    }
}
