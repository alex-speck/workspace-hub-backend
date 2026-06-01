package com.projetofullstack.workspace_hub.application.services;


import com.projetofullstack.workspace_hub.application.dto.response.UsuarioLogado;
import com.projetofullstack.workspace_hub.domain.entities.Cliente;
import com.projetofullstack.workspace_hub.domain.repository.EmpresaRepository;
import com.projetofullstack.workspace_hub.infrastructure.exceptions.ResourceNotFoundException;
import com.projetofullstack.workspace_hub.application.dto.request.ClienteAlterarStatusRequest;
import com.projetofullstack.workspace_hub.application.dto.request.ClienteRequest;
import com.projetofullstack.workspace_hub.application.dto.response.ClienteResponse;
import com.projetofullstack.workspace_hub.domain.repository.ClienteRepository;
import com.projetofullstack.workspace_hub.infrastructure.providers.UsuarioLogadoProvider;
import com.projetofullstack.workspace_hub.domain.valueobjects.CPFCNPJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<ClienteResponse> listarTodos() {
        try {
            UsuarioLogado usuarioLogado = UsuarioLogadoProvider.getUsuarioLogado();

            if (!usuarioLogado.role().equals("ADMIN") && usuarioLogado.empresaId() != null) {
                return repository.findAllByEmpresaId(usuarioLogado.empresaId()).stream().map(ClienteResponse::new).toList();
            }

            return repository.findAll().stream().map(ClienteResponse::new).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ClienteResponse buscarPorId(Long id) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId != null) {
            return repository.findByIdAndEmpresaId(id, empresaId).map(ClienteResponse::new).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));
        }
        return repository.findById(id).map(ClienteResponse::new).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));
    }

    public ClienteResponse criarCliente(ClienteRequest request) {
        try {
            var empresa = empresaRepository.getReferenceById(UsuarioLogadoProvider.getUsuarioLogado().empresaId());
            return new ClienteResponse(repository.save(new Cliente(request, empresa)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarCliente(Long id, ClienteRequest request) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId == null) {
            return false;
        }
        var cliente = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));

        cliente.setNome(request.nome());
        cliente.setDocumento(new CPFCNPJ(request.documento()));
        cliente.setTelefone(request.telefone());

        repository.save(cliente);
        return true;
    }

    public boolean atualizarStatusCliente(Long id, ClienteAlterarStatusRequest request) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId == null) {
            return false;
        }
        var cliente = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));

        cliente.setStatus(request.status());

        repository.save(cliente);
        return true;
    }
}
