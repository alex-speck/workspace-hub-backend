package com.projetofullstack.workspace_hub.application.dto.request;


public record CriarUsuarioRequest(
        String nome,
        String email,
        String senha
) {
}
