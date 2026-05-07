package com.projetofullstack.workspace_hub.application.dto.response;

import com.projetofullstack.workspace_hub.domain.entities.Empresa;

public record EmpresaResponse(
        String razaoSocial,
        String nomeFantasia
) {
    public EmpresaResponse(Empresa empresa){
        this(empresa.getRazaoSocial(), empresa.getNomeFantasia());
    }
}
