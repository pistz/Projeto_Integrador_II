package org.felipe.gestaoacolhidos.model.controller.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserResponseDTO;
import org.felipe.gestaoacolhidos.model.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @Operation(description = "Retorna uma lista com todos os usuários cadastrados", method = "GET")
    @ApiResponse(responseCode = "200", description = "Uma lista com os usuários cadastrados")
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        List<UserResponseDTO> all = userService.findAll();
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(all);
    }

    @GetMapping("/{id}")
    @Operation(description = "Retorna um usuário pelo ID", method = "GET")
    @ApiResponse(responseCode = "200", description = "Usuário com ID, email e role")
    public ResponseEntity<UserResponseDTO> getById(UUID id){
        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(user);
    }

    @PostMapping("/register")
    @Operation(description = "Cadastra novo usuário no sistema", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário já existe e não pode ser criado novamente")
    })
    public ResponseEntity<UserCreatedResponseDTO> createUser(@RequestBody UserCreateDTO dto){
        UserCreatedResponseDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Deleta um usuário", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Usuário deletado")
    public ResponseEntity deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.status(200).body("usuário: " + id + " deletado com sucesso!");
    }
}
