package com.projetofullstack.workspace_hub.application.dto.request;

import com.projetofullstack.workspace_hub.domain.entities.Usuario;

public record CriarUsuarioAdminRequest(
        String nome,
        String email,
        String senha,
        String secretKey,
        Endereco endereco
) {
    public Usuario toUsuario() {
        Usuario retorno = new Usuario();
        retorno.setNome(nome);
        retorno.setEmail(email);
        retorno.setSenha(senha);
        retorno.setRole("ADMIN");
        retorno.setCep(endereco.cep());
        retorno.setLogradouro(endereco.logradouro());
        retorno.setBairro(endereco.bairro());
        retorno.setLocalidade(endereco.localidade());
        retorno.setUf(endereco.uf());
        return retorno;
    }
}
