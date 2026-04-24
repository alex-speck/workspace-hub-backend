package com.projetofullstack.workspace_hub.services;

import com.projetofullstack.workspace_hub.model.dto.request.CriarUsuarioRequest;
import com.projetofullstack.workspace_hub.model.dto.request.LoginRequest;
import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public boolean validarUsuarioSenha(LoginRequest request){

        try{
            return repository.existsUsuarioByEmailAndSenha(request.email(), encoder.encode(request.senha()));
        }catch (Exception e){
            throw new RuntimeException("Falha ao buscar usuario");
        }
    }

    public void cadastrarUsuario(CriarUsuarioRequest request){
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(encoder.encode(request.senha()));

    }

}
