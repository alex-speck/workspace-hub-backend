package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.dto.request.UsuarioAlterarStatusRequest;
import com.projetofullstack.workspace_hub.model.dto.response.UsuarioLogadoResponse;
import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.enums.StatusUsuario;
import com.projetofullstack.workspace_hub.model.repository.UsuarioRepository;
import com.projetofullstack.workspace_hub.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "ServiÃ§o de controle de usuarios", description = "ServiÃ§o responsavel por controlar o CRUD de usuarios do sistema")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar Todos", description = "Rota chamada para listar todos os usuarios existentes no banco, retorna um array vazio se nÃ£o houver usuarios")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Busca o usuario pela id passada como parametro, caso nÃ£o encontrar devolve null")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(repository.findById(id).orElse(null));
    }

    @GetMapping("/logado")
    @Operation(summary = "Consultar usuario logado", description = "Busca usuario logado pelo token enviado")
    public ResponseEntity<UsuarioLogadoResponse> buscarUsuarioLogado(Authentication auth){
        Usuario usuario = (Usuario) auth.getPrincipal();

        if (usuario != null && usuario.getStatus() == StatusUsuario.ATIVO) {
            return ResponseEntity.ok(usuarioService.buscarUsuarioLogado(usuario.getId()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar usuario", description = "Cria e salva usuario com base nos dados enviado pelo body, devolve o ID do usuario cadastrado")
    public ResponseEntity<Long> salvar(@RequestBody Usuario usuario){
        return ResponseEntity.ok(repository.save(usuario).getId());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar por ID", description = "Atualiza o usuario com o id passado por parametro usando os dados enviados no body, retorna 404 caso o usuario nÃ£o existir")
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id, @RequestBody Usuario usuario){
        var usuarioBanco = repository.findById(id).orElse(null);

        if (usuarioBanco != null){
            usuarioBanco.setEmail(usuario.getEmail());
            usuarioBanco.setSenha(usuario.getSenha());
            usuarioBanco.setNome(usuario.getNome());
            usuarioBanco.setStatus(usuario.getStatus());

            repository.save(usuarioBanco);

            return ResponseEntity.ok("Atualizado com sucesso!");
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status", description = "Atualiza status do usuario com id informado no parametro")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id, @RequestBody UsuarioAlterarStatusRequest request){
        var usuario = repository.findById(id).orElse(null);

        if (usuario != null){
            usuario.setStatus(request.status());
            repository.save(usuario);
            return ResponseEntity.ok("Atualizado com sucesso!");
        }

        return ResponseEntity.notFound().build();
    }
}
