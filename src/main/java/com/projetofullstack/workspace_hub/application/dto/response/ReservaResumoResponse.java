package com.projetofullstack.workspace_hub.application.dto.response;

import java.util.List;

public record ReservaResumoResponse(
        List<ReservaResponse> hoje,
        List<ReservaResponse> proximos
) {
}
