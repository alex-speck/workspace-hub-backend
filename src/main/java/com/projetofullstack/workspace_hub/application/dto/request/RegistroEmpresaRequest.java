package com.projetofullstack.workspace_hub.application.dto.request;

public record RegistroEmpresaRequest(
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String email,
        String telefone,
        CriarUsuarioRequest usuarioPadrao
) {
}
