package com.projetofullstack.workspace_hub.application.dto.request;

import jakarta.validation.constraints.Email;

public record UsuarioAlterarSenhaSolicitacao(
        @Email(message = "Deve ser um email valido")
        String email
) {
}
