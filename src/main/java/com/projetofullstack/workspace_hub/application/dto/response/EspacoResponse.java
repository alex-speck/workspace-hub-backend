package com.projetofullstack.workspace_hub.application.dto.response;

import com.projetofullstack.workspace_hub.domain.entities.Espaco;
import com.projetofullstack.workspace_hub.domain.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.domain.enums.TipoEspaco;

public record EspacoResponse(
        Long id,
        String nomeNumero,
        TipoEspaco tipo,
        Double valorHora,
        StatusEspaco status
) {
    public EspacoResponse(Espaco espaco) {
        this(espaco.getId(), espaco.getNomeNumero(), espaco.getTipo(), espaco.getValorHora(), espaco.getStatus());
    }
}
