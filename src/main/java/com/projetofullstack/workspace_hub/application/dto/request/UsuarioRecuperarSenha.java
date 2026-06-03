package com.projetofullstack.workspace_hub.application.dto.request;

public record UsuarioRecuperarSenha(
        String token,
        String senha,
        String confirmacaoSenha
) {
}
