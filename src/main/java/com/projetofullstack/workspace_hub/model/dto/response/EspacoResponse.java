package com.projetofullstack.workspace_hub.model.dto.response;

import com.projetofullstack.workspace_hub.model.entities.Espaco;
import com.projetofullstack.workspace_hub.model.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.model.enums.TipoEspaco;

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
