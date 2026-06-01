package com.projetofullstack.workspace_hub.application.dto.response;

import com.projetofullstack.workspace_hub.domain.entities.Reserva;
import com.projetofullstack.workspace_hub.domain.enums.StatusReserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservaResponse(
        Long id,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim,
        Double valorTotal,
        Double valorHora,
        StatusReserva status,
        EspacoResponse espaco,
        ClienteResponse cliente
) {

    public ReservaResponse(Reserva reserva) {
        this(reserva.getId(), reserva.getData(), reserva.getHoraInicio(), reserva.getHoraFim(), reserva.getValorTotal(), reserva.getValorHora(), reserva.getStatus(), new EspacoResponse(reserva.getEspaco()), new ClienteResponse(reserva.getCliente()));
    }
}
