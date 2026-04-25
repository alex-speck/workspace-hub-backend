package com.projetofullstack.workspace_hub.model.dto.request;

import com.projetofullstack.workspace_hub.model.entities.Espaco;
import com.projetofullstack.workspace_hub.model.enums.TipoEspaco;

public record EspacoRequest(
        String nomeNumero,
        TipoEspaco tipo,
        Double valorHora
) {
    public Espaco toEspaco(){
        Espaco retorno = new Espaco();
        retorno.setNomeNumero(nomeNumero());
        retorno.setTipo(tipo());
        retorno.setValorHora(valorHora());

        return retorno;
    }
}
