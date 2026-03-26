package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.dto.request.EspacoRequest;
import com.projetofullstack.workspace_hub.model.dto.response.EspacoResponse;
import com.projetofullstack.workspace_hub.model.entities.Espaco;
import com.projetofullstack.workspace_hub.model.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.model.repository.EspacoRepository;
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
    private EspacoRepository espacoRepository;

    @GetMapping
    @Operation(summary = "Listar todos", description = "Lista todos os espaços da empresa, devolve uma lista vazia caso não haja registros")
    public ResponseEntity<List<EspacoResponse>> listarTodos() {
        return ResponseEntity.ok(espacoRepository.findAll().stream().map(EspacoResponse::new).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por id", description = "Busca o espaço baseado no id informado no parametro, se não existir retorna null")
    public ResponseEntity<EspacoResponse> buscarPorId(@PathVariable Long id){
        var espaco = espacoRepository.findById(id).orElse(null);
        if (espaco != null){
            return ResponseEntity.ok(new EspacoResponse(espaco));
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping
    @Operation(summary = "Cadastrar espaço", description = "Cadastra o espaço enviado no corpo da requisição e retorna o ID dele")
    public ResponseEntity<Long> criar(@RequestBody EspacoRequest request){
        Espaco espacoNovo = espacoRepository.save(new Espaco(null, request.nomeNumero(), request.tipo(), request.valorHora(), StatusEspaco.DISPONIVEL));

        return ResponseEntity.status(HttpStatus.CREATED).body(espacoNovo.getId());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar espaço", description = "Atualiza o espaço com id informado no parametro, retorna 404 caso não exista")
    public ResponseEntity<EspacoResponse> atualizar(@PathVariable Long id, @RequestBody EspacoRequest request){
        var espaco = espacoRepository.findById(id).orElse(null);

        if (espaco != null){
            espaco.setNomeNumero(request.nomeNumero());
            espaco.setTipo(request.tipo());
            espaco.setValorHora(request.valorHora());

            return ResponseEntity.ok(new EspacoResponse(espacoRepository.save(espaco)));
        }

        return ResponseEntity.notFound().build();
    }

}
