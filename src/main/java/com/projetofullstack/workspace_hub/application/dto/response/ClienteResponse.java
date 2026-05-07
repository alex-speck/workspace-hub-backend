package com.projetofullstack.workspace_hub.application.dto.response;

import com.projetofullstack.workspace_hub.domain.entities.Cliente;
import com.projetofullstack.workspace_hub.domain.enums.StatusCliente;

public record ClienteResponse(
        Long id,
        String nome,
        String telefone,
        String documento,
        StatusCliente status
) {
    public ClienteResponse(Cliente cliente){
        this(cliente.getId(), cliente.getNome(), cliente.getTelefone(), cliente.getDocumento(), cliente.getStatus());
    }
}
