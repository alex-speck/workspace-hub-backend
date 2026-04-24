package com.projetofullstack.workspace_hub.model.dto.request;

import com.projetofullstack.workspace_hub.model.entities.Usuario;

public record CriarUsuarioRequest(
        String nome,
        String email,
        String senha
) {
    public Usuario toUsuario() {
        Usuario retorno = new Usuario();
        retorno.setNome(nome);
        retorno.setEmail(email);
        retorno.setSenha(senha);
        return retorno;
    }
}
