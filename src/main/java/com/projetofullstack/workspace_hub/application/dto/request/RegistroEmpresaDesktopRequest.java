package com.projetofullstack.workspace_hub.application.dto.request;

public record RegistroEmpresaDesktopRequest(
        String cnpj,
        String email,
        String telefone,
        String senha
) {
}
