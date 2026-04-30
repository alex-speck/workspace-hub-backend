package com.projetofullstack.workspace_hub.services;

import com.projetofullstack.workspace_hub.exceptions.ResourceNotFoundException;
import com.projetofullstack.workspace_hub.model.dto.request.EspacoAlterarStatusRequest;
import com.projetofullstack.workspace_hub.model.dto.request.EspacoRequest;
import com.projetofullstack.workspace_hub.model.dto.response.EspacoResponse;
import com.projetofullstack.workspace_hub.model.entities.Espaco;
import com.projetofullstack.workspace_hub.model.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.model.repository.EspacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspacoService {

    @Autowired
    private EspacoRepository repository;

    public List<EspacoResponse> listarTodos() {
        try {
            return repository.findAll().stream().map(EspacoResponse::new).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public EspacoResponse buscarPorId(Long id) {
        return repository.findById(id).map(EspacoResponse::new).orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado!"));
    }

    public void salvarEspaco(EspacoRequest request) {
        try {
            Espaco espaco = request.toEspaco();
            repository.save(espaco);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarEspaco(Long id, EspacoRequest request) {
        var espaco = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado!"));

        espaco.setNomeNumero(request.nomeNumero());
        espaco.setTipo(request.tipo());
        espaco.setValorHora(request.valorHora());

        repository.save(espaco);
        return true;
    }

    public boolean atualizarStatusEspaco(Long id, EspacoAlterarStatusRequest request) {
        var espaco = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado!"));

        espaco.setStatus(request.status());
        repository.save(espaco);

        return true;
    }

    public boolean deletarEspacoPorId(Long id) {
        var espaco = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado!"));

        espaco.setStatus(StatusEspaco.DELETADO);
        repository.save(espaco);

        return true;
    }
}
