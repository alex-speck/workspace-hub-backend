package com.projetofullstack.workspace_hub.application.dto.request;

import com.projetofullstack.workspace_hub.domain.enums.TipoEspaco;
import com.projetofullstack.workspace_hub.domain.valueobjects.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record EspacoRequest(
        @NotBlank(message = "Deve informar um nome/numero")
        String nomeNumero,
        TipoEspaco tipo,
        @Positive(message = "O valor deve ser positivo ou zero")
        Double valorHora,
        Endereco endereco
) {
}
