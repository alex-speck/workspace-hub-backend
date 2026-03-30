package com.projetofullstack.workspace_hub.model.dto.request;

import com.projetofullstack.workspace_hub.model.enums.StatusCliente;

public record ClienteAlterarStatusRequest(
        StatusCliente status
) {
}
