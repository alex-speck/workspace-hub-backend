package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.dto.request.ClienteAlterarStatusRequest;
import com.projetofullstack.workspace_hub.model.dto.request.ClienteRequest;
import com.projetofullstack.workspace_hub.model.dto.response.ClienteResponse;
import com.projetofullstack.workspace_hub.model.dto.response.EspacoResponse;
import com.projetofullstack.workspace_hub.model.entities.Cliente;
import com.projetofullstack.workspace_hub.model.enums.StatusCliente;
import com.projetofullstack.workspace_hub.model.repository.ClienteRepository;
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
    private ClienteRepository clienteRepository;

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Lista todos os clientes presentes no banco de dados")
    public ResponseEntity<List<ClienteResponse>> listarTodos(){
        return ResponseEntity.ok(clienteRepository.findAll().stream().map(ClienteResponse::new).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por id", description = "Lista o cliente com id informado ou senao retorna 404 caso não existir")
    public ResponseEntity<ClienteResponse> buscarPorId (@PathVariable Long id){
        var cliente = clienteRepository.findById(id).orElse(null);

        if (cliente != null){
            return ResponseEntity.ok(new ClienteResponse(cliente));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar cliente", description = "Cria um usuario com base nos dados informados no corpo da requisição")
    public ResponseEntity<ClienteResponse> criar(@RequestBody ClienteRequest request){
        Cliente novoCliente = clienteRepository.save(new Cliente(null, request.nome(), request.documento(), request.telefone(), StatusCliente.ATIVO));

        return ResponseEntity.ok(new ClienteResponse(novoCliente));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do cliente", description = "Atualiza os dados do cliente excluindo o status")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request){
        var cliente = clienteRepository.findById(id).orElse(null);

        if (cliente != null){
            cliente.setNome(request.nome());
            cliente.setDocumento(request.documento());
            cliente.setTelefone(request.telefone());

            return ResponseEntity.ok(new ClienteResponse(clienteRepository.save(cliente)));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status cliente", description = "Atualiza o status da cliente")
    public ResponseEntity<ClienteResponse> atualizarStatus(@PathVariable Long id, @RequestBody ClienteAlterarStatusRequest request){
        var cliente = clienteRepository.findById(id).orElse(null);

        if (cliente != null){
            cliente.setStatus(request.status());

            return ResponseEntity.ok(new ClienteResponse(clienteRepository.save(cliente)));
        }

        return ResponseEntity.notFound().build();
    }

}
