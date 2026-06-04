package com.projetofullstack.workspace_hub.application.dto.response;

import com.projetofullstack.workspace_hub.application.dto.request.EnderecoRequest;
import com.projetofullstack.workspace_hub.domain.valueobjects.Endereco;

public record EnderecoResponse(
        String logradouro,
        String numero,
        String bairro,
        String cidade,
        String uf,
        String cep
) {
    public EnderecoResponse(Endereco endereco){
        this(endereco.getLogradouro(), endereco.getNumero(), endereco.getBairro(), endereco.getCidade(), endereco.getUf(), endereco.getCep());
    }
}
