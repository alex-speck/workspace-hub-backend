package com.projetofullstack.workspace_hub.presentation;

import com.projetofullstack.workspace_hub.application.dto.request.*;
import com.projetofullstack.workspace_hub.application.dto.response.LoginResponse;
import com.projetofullstack.workspace_hub.application.services.EmpresaService;
import com.projetofullstack.workspace_hub.application.services.TokenService;
import com.projetofullstack.workspace_hub.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @PostMapping("/cadastro/desktop")
    @Operation(summary = "Cadastro da primeira empresa", description = "Cadastra a primeira empresa do sistema pela interface JavaFX")
    public ResponseEntity<?> cadastrarEmpresaDesktop(@RequestBody RegistroEmpresaDesktopRequest request){
        empresaService.cadastrarEmpresaDesktop(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/recuperar-senha")
    @Operation(summary = "Pedido de recuperação de senha", description = "Recebe um email, se existir um usuario com esse email envia um link por email para recuperar a senha")
    public ResponseEntity<?> recuperarSenha(@RequestBody UsuarioAlterarSenhaSolicitacao request){
        usuarioService.criarAlterarSenha(request.email());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/alterar-senha")
    @Operation(summary = "Alterar senha do usuario")
    public ResponseEntity<?> alterarSenha(@RequestBody UsuarioRecuperarSenha request){
        var sucesso = usuarioService.recuperarSenha(request);

        return sucesso ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
