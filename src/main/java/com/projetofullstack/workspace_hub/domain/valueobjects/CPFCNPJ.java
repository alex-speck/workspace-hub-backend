package com.projetofullstack.workspace_hub.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class CPFCNPJ {

    @Column(name = "documento")
    private String valor;

    public CPFCNPJ(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("CPF/CNPJ não pode ser nulo!");
        }
        String cleanValor = valor.replaceAll("[^0-9]", "");
        if (cleanValor.length() == 11) {
            this.valor = new CPF(cleanValor).getValor();
        } else if (cleanValor.length() == 14) {
            this.valor = new CNPJ(cleanValor).getValor();
        } else {
            throw new IllegalArgumentException("CPF/CNPJ inválido!");
        }
    }

    public String getValor() {
        return valor;
    }

    public boolean isCnpj() {
        return valor.length() == 14;
    }

    public boolean isCpf() {
        return valor.length() == 11;
    }

    public String toFormattedString() {
        if (!isCnpj()) {
            return new CPF(valor).toFormattedString();
        }
        return new CNPJ(valor).toFormattedString();
    }

    public String toMaskedString() {
        if (!isCnpj()) {
            return new CPF(valor).toMaskedString();
        }
        return new CNPJ(valor).toMaskedString();
    }

    @Override
    public String toString() {
        return toFormattedString();
    }

}
