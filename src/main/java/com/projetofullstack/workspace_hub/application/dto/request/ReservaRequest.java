package com.projetofullstack.workspace_hub.application.dto.request;

import java.time.LocalDateTime;

public record ReservaRequest(
        Long clienteId,
        Long espacoId,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim
) {
}
