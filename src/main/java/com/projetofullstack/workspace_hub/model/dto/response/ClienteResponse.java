package com.projetofullstack.workspace_hub.model.dto.response;

import com.projetofullstack.workspace_hub.model.entities.Cliente;
import com.projetofullstack.workspace_hub.model.enums.StatusCliente;

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
