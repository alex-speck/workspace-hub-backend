package com.projetofullstack.workspace_hub.services;

import com.projetofullstack.workspace_hub.exceptions.ResourceNotFoundException;
import com.projetofullstack.workspace_hub.model.dto.request.AtualizarUsuarioRequest;
import com.projetofullstack.workspace_hub.model.dto.request.CriarUsuarioRequest;
import com.projetofullstack.workspace_hub.model.dto.request.LoginRequest;
import com.projetofullstack.workspace_hub.model.dto.request.UsuarioAlterarStatusRequest;
import com.projetofullstack.workspace_hub.model.dto.response.UsuarioLogadoResponse;
import com.projetofullstack.workspace_hub.model.dto.response.UsuarioResponse;
import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public boolean validarUsuarioSenha(LoginRequest request) {
        try {
            return repository.findByEmail(request.email())
                    .map(usuario -> encoder.matches(request.senha(), usuario.getSenha()))
                    .orElse(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioLogadoResponse buscarUsuarioLogado(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        return new UsuarioLogadoResponse(usuario, usuario.getEmpresa());
    }

    public List<UsuarioResponse> listarTodos() {
        try {
            return repository.findAll().stream().map(UsuarioResponse::new).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public UsuarioResponse buscarPorId(Long id) {
        return repository.findById(id).map(UsuarioResponse::new).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado!"));
    }

    public UsuarioResponse salvarNovoUsuario(CriarUsuarioRequest request) {
        Usuario usuario = request.toUsuario();
        try {
            return new UsuarioResponse(repository.save(usuario));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarUsuario(Long id, AtualizarUsuarioRequest request) {
        var usuarioBanco = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        usuarioBanco.setEmail(request.email());
        usuarioBanco.setNome(request.nome());

        repository.save(usuarioBanco);

        return true;
    }

    public boolean atualizarStatusUsuario(Long id, UsuarioAlterarStatusRequest request) {
        var usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        usuario.setStatus(request.status());
        repository.save(usuario);
        return true;
    }
}
