package org.felipe.gestaoacolhidos.model.controller.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Endpoints de Usuário")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/find/all")
    @Operation(description = "Retorna uma lista com todos os usuários cadastrados", method = "GET")
    @ApiResponse(responseCode = "200", description = "Uma lista com os usuários cadastrados")
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        List<UserResponseDTO> all = userService.findAll();
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(all);
    }

    @GetMapping("/find/{id}")
    @Operation(description = "Retorna um usuário pelo ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário com ID, email e role"),
            @ApiResponse(responseCode = "404", description = "Id fornecido não existe")
    })
    public ResponseEntity<UserResponseDTO> getById(@PathVariable("id") String id){
        UserResponseDTO user = userService.findById(UUID.fromString(id));
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(user);
    }

    @PostMapping("/register")
    @Operation(description = "Cadastra novo usuário no sistema", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "406", description = "Usuário já existe e não pode ser criado novamente"),
            @ApiResponse(responseCode = "400", description = "E-mail utilizado para cadastro não é válido")
    })
    public ResponseEntity<UserCreatedResponseDTO> createUser(@RequestBody UserCreateDTO dto){
        UserCreatedResponseDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(created);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um usuário", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado"),
            @ApiResponse(responseCode = "400", description = "O ID é nulo"),
            @ApiResponse(responseCode = "404", description = "O usuário não existe pelo ID fornecido")
    })
    public ResponseEntity deleteUser(@PathVariable("id") String id){
        userService.deleteUser(UUID.fromString(id));
        return ResponseEntity.status(200).body("usuário: " + id + " deletado com sucesso!");
    }
}
