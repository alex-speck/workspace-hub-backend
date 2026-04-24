package com.projetofullstack.workspace_hub.model.dto.request;

public record CriarUsuarioRequest(
        String nome,
        String email,
        String senha
) {
}
