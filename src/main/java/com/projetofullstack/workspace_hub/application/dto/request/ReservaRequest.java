package com.projetofullstack.workspace_hub.application.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaRequest(
        Long clienteId,
        Long espacoId,
        @Future(message = "A data deve ser no futuro")
        @NotNull(message = "A data não pode ser nula")
        LocalDate data,
        @NotNull(message = "Os horarios devem ser informados")
        LocalTime horaInicio,
        @NotNull(message = "Os horarios devem ser informados")
        LocalTime horaFim
) {
}
