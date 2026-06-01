package com.projetofullstack.workspace_hub.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroEmpresaRequest(
        @NotBlank(message = "Deve informar razão social")
        String razaoSocial,
        @NotNull(message = "Deve informar nome fantasia")
        String nomeFantasia,
        @NotBlank(message = "Deve informar cnpj")
        String cnpj,
        @NotBlank(message = "Deve informar email")
        @Email(message = "Deve ser um email valido")
        String email,
        @NotBlank(message = "Deve informar telefone")
        String telefone,
        CriarUsuarioRequest usuarioPadrao
) {
}
