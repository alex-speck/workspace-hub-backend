package com.projetofullstack.workspace_hub.model.dto.response;

import com.projetofullstack.workspace_hub.model.entities.Empresa;

public record EmpresaResponse(
        String razaoSocial,
        String nomeFantasia
) {
    public EmpresaResponse(Empresa empresa){
        this(empresa.getRazaoSocial(), empresa.getNomeFantasia());
    }
}
