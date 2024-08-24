package org.felipe.gestaoacolhidos.model.controller.Auth;

import org.felipe.gestaoacolhidos.model.dto.User.UserLoginDTO;
import org.felipe.gestaoacolhidos.model.service.Auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody UserLoginDTO dto) {
            var auth = authService.authenticateUser(dto.email(), dto.password());
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(auth.toString());
    }

}
