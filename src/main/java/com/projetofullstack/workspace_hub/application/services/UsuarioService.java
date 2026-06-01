package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.application.dto.request.*;
import com.projetofullstack.workspace_hub.application.dto.response.UsuarioLogado;
import com.projetofullstack.workspace_hub.domain.repository.EmpresaRepository;
import com.projetofullstack.workspace_hub.infrastructure.exceptions.ResourceNotFoundException;
import com.projetofullstack.workspace_hub.application.dto.response.UsuarioLogadoResponse;
import com.projetofullstack.workspace_hub.application.dto.response.UsuarioResponse;
import com.projetofullstack.workspace_hub.domain.entities.Usuario;
import com.projetofullstack.workspace_hub.domain.repository.UsuarioRepository;
import com.projetofullstack.workspace_hub.infrastructure.providers.UsuarioLogadoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmpresaRepository empresaRepository;

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
            UsuarioLogado usuarioLogado = UsuarioLogadoProvider.getUsuarioLogado();

            if (!usuarioLogado.role().equals("ADMIN") && usuarioLogado.empresaId() != null) {
                return repository.findAllByEmpresaId(usuarioLogado.empresaId()).stream().map(UsuarioResponse::new).toList();
            }

            return repository.findAll().stream().map(UsuarioResponse::new).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public UsuarioResponse buscarPorId(Long id) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId != null) {
            return repository.findByIdAndEmpresaId(id, empresaId).map(UsuarioResponse::new).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado!"));
        }
        return repository.findById(id).map(UsuarioResponse::new).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado!"));
    }

    public UsuarioResponse salvarNovoUsuario(CriarUsuarioRequest request) {
        try {
            var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
            var empresa = empresaRepository.getReferenceById(empresaId);

            Usuario usuario = new Usuario(request, empresa);
            usuario.setSenha(encoder.encode(usuario.getSenha()));
            return new UsuarioResponse(repository.save(usuario));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarUsuario(Long id, AtualizarUsuarioRequest request) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId == null) {
            return false;
        }
        var usuarioBanco = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        usuarioBanco.setEmail(request.email());
        usuarioBanco.setNome(request.nome());

        repository.save(usuarioBanco);

        return true;
    }

    public boolean atualizarStatusUsuario(Long id, UsuarioAlterarStatusRequest request) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId == null) {
            return false;
        }
        var usuario = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        usuario.setStatus(request.status());
        repository.save(usuario);
        return true;
    }

    public UsuarioResponse salvarUsuarioAdmin(CriarUsuarioAdminRequest request) {

        if (!request.secretKey().equals(secret)){
            return new UsuarioResponse(0L, "", "", null);
        }

        Usuario usuario = new Usuario(request);
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        return new UsuarioResponse(repository.save(usuario));
    }
}
