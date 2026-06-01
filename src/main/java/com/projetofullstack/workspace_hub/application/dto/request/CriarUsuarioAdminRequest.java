package com.projetofullstack.workspace_hub.application.dto.request;

import com.projetofullstack.workspace_hub.domain.entities.Usuario;

public record CriarUsuarioAdminRequest(
        String nome,
        String email,
        String senha,
        String secretKey
) {
}
