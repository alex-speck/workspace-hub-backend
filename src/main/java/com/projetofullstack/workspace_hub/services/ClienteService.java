package com.projetofullstack.workspace_hub.services;


import com.projetofullstack.workspace_hub.exceptions.ResourceNotFoundException;
import com.projetofullstack.workspace_hub.model.dto.request.ClienteAlterarStatusRequest;
import com.projetofullstack.workspace_hub.model.dto.request.ClienteRequest;
import com.projetofullstack.workspace_hub.model.dto.response.ClienteResponse;
import com.projetofullstack.workspace_hub.model.entities.Cliente;
import com.projetofullstack.workspace_hub.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<ClienteResponse> listarTodos() {
        try {
            return repository.findAll().stream().map(ClienteResponse::new).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ClienteResponse buscarPorId(Long id) {
        return repository.findById(id).map(ClienteResponse::new).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));
    }

    public ClienteResponse criarCliente(ClienteRequest request) {
        try {
            return new ClienteResponse(repository.save(request.toCliente()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarCliente(Long id, ClienteRequest request) {
        var cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));

        cliente.setNome(request.nome());
        cliente.setDocumento(request.documento());
        cliente.setTelefone(request.telefone());

        repository.save(cliente);
        return true;
    }

    public boolean atualizarStatusCliente(Long id, ClienteAlterarStatusRequest request) {
        var cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));

        cliente.setStatus(request.status());

        repository.save(cliente);
        return true;
    }
}
