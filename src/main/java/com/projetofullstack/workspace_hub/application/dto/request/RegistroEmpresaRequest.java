package com.projetofullstack.workspace_hub.application.dto.request;

import com.projetofullstack.workspace_hub.domain.valueobjects.CNPJ;

public record RegistroEmpresaRequest(
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String email,
        String telefone,
        CriarUsuarioRequest usuarioPadrao
) {
}
