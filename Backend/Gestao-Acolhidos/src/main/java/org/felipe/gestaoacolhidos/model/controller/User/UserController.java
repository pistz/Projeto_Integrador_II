package org.felipe.gestaoacolhidos.model.controller.User;

import org.felipe.gestaoacolhidos.model.dto.User.UserCreateDTO;
import org.felipe.gestaoacolhidos.model.dto.User.UserCreatedResponseDTO;
import org.felipe.gestaoacolhidos.model.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserCreatedResponseDTO> createUser(@RequestBody UserCreateDTO dto){
        UserCreatedResponseDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(created);
    }
}
