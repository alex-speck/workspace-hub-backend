package com.projetofullstack.workspace_hub.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Endereco {

    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

    public Endereco(Endereco endereco){
        this.logradouro = endereco.logradouro;
        this.numero = endereco.numero;
        this.bairro = endereco.bairro;
        this.cidade = endereco.cidade;
        this.uf = endereco.uf;
        this.cep = endereco.cep;
    }

    @Override
    public String toString() {
        return String.format(
                "%s, %s - %s%n%s, %s/%s",
                logradouro,
                numero,
                bairro,
                cep,
                cidade,
                uf
        );
    }
}
