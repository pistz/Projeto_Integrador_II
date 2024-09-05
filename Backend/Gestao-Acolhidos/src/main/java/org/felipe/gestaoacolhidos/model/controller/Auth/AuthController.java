package org.felipe.gestaoacolhidos.model.controller.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.felipe.gestaoacolhidos.model.domain.dto.User.UserLoginDTO;
import org.felipe.gestaoacolhidos.model.domain.service.Auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Endpoint de Autenticação")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(description = "Realiza a autenticação para login", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem sucedida"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<String> login(@RequestBody UserLoginDTO dto) {
            var auth = authService.authenticateUser(dto.email(), dto.password());
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(auth.toString());
    }

}
