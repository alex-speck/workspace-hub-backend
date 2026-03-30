package com.projetofullstack.workspace_hub.model.dto.request;

import com.projetofullstack.workspace_hub.model.enums.StatusEspaco;

public record EspacoAlterarStatusRequest(
        StatusEspaco status
) {
}
