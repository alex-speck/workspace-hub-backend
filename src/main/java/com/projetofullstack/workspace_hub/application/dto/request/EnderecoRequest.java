package com.projetofullstack.workspace_hub.application.dto.request;

public record EnderecoRequest(
        String logradouro,
        String numero,
        String bairro,
        String cidade,
        String uf,
        String cep
) {
}
