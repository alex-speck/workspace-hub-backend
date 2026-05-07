package com.projetofullstack.workspace_hub.application.dto.request;

import com.projetofullstack.workspace_hub.domain.enums.StatusCliente;

public record ClienteAlterarStatusRequest(
        StatusCliente status
) {
}
