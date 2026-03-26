package com.projetofullstack.workspace_hub.model.dto.request;

import com.projetofullstack.workspace_hub.model.enums.TipoEspaco;

public record EspacoRequest(
        String nomeNumero,
        TipoEspaco tipo,
        Double valorHora
) {
}
