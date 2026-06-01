package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.infrastructure.exceptions.ResourceNotFoundException;
import com.projetofullstack.workspace_hub.application.dto.request.EspacoAlterarStatusRequest;
import com.projetofullstack.workspace_hub.application.dto.request.EspacoRequest;
import com.projetofullstack.workspace_hub.application.dto.response.EspacoResponse;
import com.projetofullstack.workspace_hub.application.dto.response.UsuarioLogado;
import com.projetofullstack.workspace_hub.domain.entities.Espaco;
import com.projetofullstack.workspace_hub.domain.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.domain.repository.EmpresaRepository;
import com.projetofullstack.workspace_hub.domain.repository.EspacoRepository;
import com.projetofullstack.workspace_hub.infrastructure.providers.UsuarioLogadoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspacoService {

    @Autowired
    private EspacoRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<EspacoResponse> listarTodos() {
        try {
            UsuarioLogado usuarioLogado = UsuarioLogadoProvider.getUsuarioLogado();

            if (!usuarioLogado.role().equals("ADMIN") && usuarioLogado.empresaId() != null) {
                return repository.findAllByEmpresaId(usuarioLogado.empresaId()).stream().map(EspacoResponse::new).toList();
            }

            return repository.findAll().stream().map(EspacoResponse::new).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public EspacoResponse buscarPorId(Long id) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId != null) {
            return repository.findByIdAndEmpresaId(id, empresaId).map(EspacoResponse::new).orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado!"));
        }
        return repository.findById(id).map(EspacoResponse::new).orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado!"));
    }

    public void salvarEspaco(EspacoRequest request) {
        try {
            var empresa = empresaRepository.getReferenceById(UsuarioLogadoProvider.getUsuarioLogado().empresaId());
            Espaco espaco = new Espaco(request, empresa);
            repository.save(espaco);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarEspaco(Long id, EspacoRequest request) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId == null) {
            return false;
        }
        var espaco = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado!"));

        espaco.setNomeNumero(request.nomeNumero());
        espaco.setTipo(request.tipo());
        espaco.setValorHora(request.valorHora());

        repository.save(espaco);
        return true;
    }

    public boolean atualizarStatusEspaco(Long id, EspacoAlterarStatusRequest request) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId == null) {
            return false;
        }
        var espaco = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado!"));

        espaco.setStatus(request.status());
        repository.save(espaco);

        return true;
    }

    public boolean deletarEspacoPorId(Long id) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId == null) {
            return false;
        }
        var espaco = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado!"));

        espaco.setStatus(StatusEspaco.DELETADO);
        repository.save(espaco);

        return true;
    }
}
