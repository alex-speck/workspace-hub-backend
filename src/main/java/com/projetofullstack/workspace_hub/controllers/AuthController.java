package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.dto.LoginRequest;
import com.projetofullstack.workspace_hub.model.dto.LoginResponse;
import com.projetofullstack.workspace_hub.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        if (loginRequest.email().equals("alexbaranoski08@gmail.com") && loginRequest.senha().equals("1234")){
            return ResponseEntity.ok(new LoginResponse("tokensuperseguro"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}
