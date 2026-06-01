package com.projetofullstack.workspace_hub.application.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarUsuarioRequest(
        @NotBlank(message = "O nome deve ser informado")
        String nome,
        @NotBlank(message = "O email deve ser informado")
        @Email(message = "Deve ser um email valido")
        String email,
        @NotBlank(message = "A senha deve ser informada")
        @Size(min = 6, message = "A senha deve ter no minimo 6 caracteres")
        String senha
) {
}
