package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.dto.request.EspacoAlterarStatusRequest;
import com.projetofullstack.workspace_hub.model.dto.request.EspacoRequest;
import com.projetofullstack.workspace_hub.model.dto.response.EspacoResponse;
import com.projetofullstack.workspace_hub.model.entities.Espaco;
import com.projetofullstack.workspace_hub.model.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.model.repository.EspacoRepository;
import com.projetofullstack.workspace_hub.services.EspacoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/espacos")
@Tag(name = "Serviço de Espaços", description = "Serviço responsavel pelo CRUD completo de espaços da empresa")
public class EspacoController {

    @Autowired
    private EspacoService espacoService;

    @GetMapping
    @Operation(summary = "Listar todos", description = "Lista todos os espaços da empresa, devolve uma lista vazia caso não haja registros")
    public ResponseEntity<List<EspacoResponse>> listarTodos() {
        return ResponseEntity.ok(espacoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por id", description = "Busca o espaço baseado no id informado no parametro, se não existir retorna null")
    public ResponseEntity<EspacoResponse> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(espacoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar espaço", description = "Cadastra o espaço enviado no corpo da requisição e retorna o ID dele")
    public ResponseEntity<?> criar(@RequestBody EspacoRequest request){
        espacoService.salvarEspaco(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar espaço", description = "Atualiza o espaço com id informado no parametro, retorna 404 caso não exista")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody EspacoRequest request){
        espacoService.atualizarEspaco(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status entrega", description = "Atualiza o status da entrega")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody EspacoAlterarStatusRequest request){
        espacoService.atualizarStatusEspaco(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar espaço", description = "Não apaga os dados do espaço do banco apenas seta o status para DELETADO")
    public ResponseEntity<?> deletarPorId(@PathVariable Long id){
        espacoService.deletarEspacoPorId(id);
        return ResponseEntity.ok().build();
    }

}
