package com.projetofullstack.workspace_hub.services;


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
        try {
            return repository.findById(id).map(ClienteResponse::new).orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ClienteResponse criarCliente(ClienteRequest request) {
        try {
            return new ClienteResponse(repository.save(request.toCliente()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarCliente(Long id, ClienteRequest request) {
        try {
            var cliente = repository.findById(id).orElse(null);

            if (cliente != null) {
                cliente.setNome(request.nome());
                cliente.setDocumento(request.documento());
                cliente.setTelefone(request.telefone());

                repository.save(cliente);
                return true;
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarStatusCliente(Long id, ClienteAlterarStatusRequest request) {
        try {
            var cliente = repository.findById(id).orElse(null);

            if (cliente != null){
                cliente.setStatus(request.status());

                repository.save(cliente);
                return true;
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
