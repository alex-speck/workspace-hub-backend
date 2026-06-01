package com.projetofullstack.workspace_hub.application.dto.request;

import com.projetofullstack.workspace_hub.domain.entities.Espaco;
import com.projetofullstack.workspace_hub.domain.enums.TipoEspaco;

public record EspacoRequest(
        String nomeNumero,
        TipoEspaco tipo,
        Double valorHora
) {
}
