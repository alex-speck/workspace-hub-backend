package com.projetofullstack.workspace_hub.application.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record CancelamentoReservaResumo(
        String nomeEmpresa,
        String nomeCliente,
        LocalDate dataReserva,
        LocalTime horaInicio,
        LocalTime horaFim,
        String codigoReserva,
        String localizacao,
        String espaco
) {
}
