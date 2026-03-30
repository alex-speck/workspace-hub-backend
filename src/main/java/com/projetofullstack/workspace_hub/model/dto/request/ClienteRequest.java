package com.projetofullstack.workspace_hub.model.dto.request;

public record ClienteRequest(
        String nome,
        String telefone,
        String documento
) {
}
