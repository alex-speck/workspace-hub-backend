package com.projetofullstack.workspace_hub.application.dto.request;


public record ClienteRequest(
        String nome,
        String telefone,
        String documento
) {

}
