package com.projetofullstack.workspace_hub.application.dto.request;

import com.projetofullstack.workspace_hub.domain.enums.StatusEspaco;

public record EspacoAlterarStatusRequest(
        StatusEspaco status
) {
}
