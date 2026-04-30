package com.projetofullstack.workspace_hub.model.dto.response;

import com.projetofullstack.workspace_hub.model.entities.Reserva;
import com.projetofullstack.workspace_hub.model.enums.StatusReserva;

import java.time.LocalDateTime;

public record ReservaResponse(
        Long id,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        Double valorTotal,
        Double valorHora,
        StatusReserva status,
        EspacoResponse espaco,
        ClienteResponse cliente
) {

    public ReservaResponse(Reserva reserva) {
        this(reserva.getId(), reserva.getDataHoraInicio(), reserva.getDataHoraFim(), reserva.getValorTotal(), reserva.getValorHora(), reserva.getStatus(), new EspacoResponse(reserva.getEspaco()), new ClienteResponse(reserva.getCliente()));
    }
}
