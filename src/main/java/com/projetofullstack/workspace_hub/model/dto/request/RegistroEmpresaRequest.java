package com.projetofullstack.workspace_hub.model.dto.request;

public record RegistroEmpresaRequest(
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String email,
        String telefone,
        CriarUsuarioRequest usuarioPadrao
) {
}
