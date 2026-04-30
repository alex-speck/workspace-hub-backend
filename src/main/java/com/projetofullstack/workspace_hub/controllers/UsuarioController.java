package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.dto.request.AtualizarUsuarioRequest;
import com.projetofullstack.workspace_hub.model.dto.request.CriarUsuarioRequest;
import com.projetofullstack.workspace_hub.model.dto.request.UsuarioAlterarStatusRequest;
import com.projetofullstack.workspace_hub.model.dto.response.UsuarioLogadoResponse;
import com.projetofullstack.workspace_hub.model.dto.response.UsuarioResponse;
import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.enums.StatusUsuario;
import com.projetofullstack.workspace_hub.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Serviço de controle de usuarios", description = "Serviço responsavel por controlar o CRUD de usuarios do sistema")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar Todos", description = "Rota chamada para listar todos os usuarios existentes no banco, retorna um array vazio se não houver usuarios")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Busca o usuario pela id passada como parametro, caso não encontrar devolve null")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping("/logado")
    @Operation(summary = "Consultar usuario logado", description = "Busca usuario logado pelo token enviado")
    public ResponseEntity<UsuarioLogadoResponse> buscarUsuarioLogado(Authentication auth){
        Usuario usuario = (Usuario) auth.getPrincipal();
        return usuario != null && usuario.getStatus() == StatusUsuario.ATIVO ? ResponseEntity.ok(usuarioService.buscarUsuarioLogado(usuario.getId())) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar usuario", description = "Cria e salva usuario com base nos dados enviado pelo body, devolve o ID do usuario cadastrado")
    public ResponseEntity<UsuarioResponse> salvar(@RequestBody CriarUsuarioRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvarNovoUsuario(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar por ID", description = "Atualiza o usuario com o id passado por parametro usando os dados enviados no body, retorna 404 caso o usuario não existir")
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id, @RequestBody AtualizarUsuarioRequest request){
        usuarioService.atualizarUsuario(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status", description = "Atualiza status do usuario com id informado no parametro")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id, @RequestBody UsuarioAlterarStatusRequest request){
        usuarioService.atualizarStatusUsuario(id, request);
        return ResponseEntity.ok().build();
    }
}
