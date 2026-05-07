package com.projetofullstack.workspace_hub.application.dto.request;

import com.projetofullstack.workspace_hub.domain.enums.StatusUsuario;

public record UsuarioAlterarStatusRequest(
        StatusUsuario status
) {
}
