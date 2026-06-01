package com.projetofullstack.workspace_hub.application.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank(message = "O nome não pode ser vazio")
        String nome,
        @NotBlank(message = "O telefone deve ser informado")
        String telefone,
        @NotBlank(message = "O documento deve ser informado")
        String documento,
        @NotBlank(message = "O email deve ser informado")
        @Email(message = "Deve ser um email valido")
        String email
) {

}
