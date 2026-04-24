package com.projetofullstack.workspace_hub.services;

import com.projetofullstack.workspace_hub.model.dto.request.CriarUsuarioRequest;
import com.projetofullstack.workspace_hub.model.dto.request.LoginRequest;
import com.projetofullstack.workspace_hub.model.dto.response.UsuarioLogadoResponse;
import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public boolean validarUsuarioSenha(LoginRequest request){
        return repository.findByEmail(request.email())
                .map(usuario -> encoder.matches(request.senha(), usuario.getSenha()))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public UsuarioLogadoResponse buscarUsuarioLogado(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nÃ£o encontrado"));

        return new UsuarioLogadoResponse(usuario, usuario.getEmpresa());
    }

    public void cadastrarUsuario(CriarUsuarioRequest request){
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(encoder.encode(request.senha()));
        repository.save(usuario);
    }
}
