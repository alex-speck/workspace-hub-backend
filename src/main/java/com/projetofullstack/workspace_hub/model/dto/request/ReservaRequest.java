package com.projetofullstack.workspace_hub.model.dto.request;

import java.time.LocalDateTime;

public record ReservaRequest(
        Long clienteId,
        Long espacoId,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim
) {
}
