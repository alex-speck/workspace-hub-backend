package com.projetofullstack.workspace_hub.model.dto.request;

import com.projetofullstack.workspace_hub.model.entities.Cliente;

public record ClienteRequest(
        String nome,
        String telefone,
        String documento
) {
    public Cliente toCliente(){
        Cliente retorno = new Cliente();
        retorno.setNome(nome());
        retorno.setTelefone(telefone());
        retorno.setDocumento(documento());

        return retorno;
    }
}
