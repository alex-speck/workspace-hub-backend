package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.dto.request.ClienteAlterarStatusRequest;
import com.projetofullstack.workspace_hub.model.dto.request.ClienteRequest;
import com.projetofullstack.workspace_hub.model.dto.response.ClienteResponse;
import com.projetofullstack.workspace_hub.model.dto.response.EspacoResponse;
import com.projetofullstack.workspace_hub.model.entities.Cliente;
import com.projetofullstack.workspace_hub.model.enums.StatusCliente;
import com.projetofullstack.workspace_hub.model.repository.ClienteRepository;
import com.projetofullstack.workspace_hub.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Serviço de clientes", description = "Controle das informações do cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Lista todos os clientes presentes no banco de dados")
    public ResponseEntity<List<ClienteResponse>> listarTodos(){
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por id", description = "Lista o cliente com id informado ou senao retorna 404 caso não existir")
    public ResponseEntity<ClienteResponse> buscarPorId (@PathVariable Long id){
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criar cliente", description = "Cria um usuario com base nos dados informados no corpo da requisição")
    public ResponseEntity<ClienteResponse> criar(@RequestBody ClienteRequest request){
        return ResponseEntity.ok(clienteService.criarCliente(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do cliente", description = "Atualiza os dados do cliente excluindo o status")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request){
        clienteService.atualizarCliente(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status cliente", description = "Atualiza o status da cliente")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody ClienteAlterarStatusRequest request){
        clienteService.atualizarStatusCliente(id, request);
        return ResponseEntity.ok().build();
    }

}
