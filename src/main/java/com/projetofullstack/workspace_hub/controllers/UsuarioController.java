package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;


    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(repository.findById(id).orElse(null));
    }

    @PostMapping
    public ResponseEntity<Long> salvar(@RequestBody Usuario usuario){
        return ResponseEntity.ok(repository.save(usuario).getId());
    }

    @PutMapping("/{id}")
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

}
