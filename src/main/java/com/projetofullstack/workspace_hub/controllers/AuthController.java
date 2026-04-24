package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.dto.request.LoginRequest;
import com.projetofullstack.workspace_hub.model.dto.request.RegistroEmpresaRequest;
import com.projetofullstack.workspace_hub.model.dto.response.LoginResponse;
import com.projetofullstack.workspace_hub.model.repository.UsuarioRepository;
import com.projetofullstack.workspace_hub.services.EmpresaService;
import com.projetofullstack.workspace_hub.services.TokenService;
import com.projetofullstack.workspace_hub.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(description = "Serviço responsavel por controlar a autenticação de usuarios e sessão!", name = "Serviço de autenticação")
public class AuthController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EmpresaService empresaService;

    @PostMapping("/login")
    @Operation(description = "Valida se o email e senha informados consta no banco de dados", summary = "Login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (usuarioService.validarUsuarioSenha(loginRequest)) {
            var token = tokenService.gerarToken(loginRequest.email());

            return ResponseEntity.ok(new LoginResponse(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastro de empresa", description = "Cadastra empresa e usuario padrã")
    public ResponseEntity<?> cadastrarEmpresa(@RequestBody RegistroEmpresaRequest request){
        empresaService.cadastrarEmpresa(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
