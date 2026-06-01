package com.projetofullstack.workspace_hub.application.dto.request;


import com.projetofullstack.workspace_hub.domain.valueobjects.CPFCNPJ;

public record ClienteRequest(
        String nome,
        String telefone,
        String documento
) {

}
