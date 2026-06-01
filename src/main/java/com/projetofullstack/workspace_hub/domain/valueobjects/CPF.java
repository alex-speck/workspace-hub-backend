package com.projetofullstack.workspace_hub.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class CPF {

    @Column(name = "cpf")
    private String valor;

    public CPF(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo!");
        }
        String cleanValor = valor.replaceAll("[^0-9]", "");
        if (!isValid(cleanValor)) {
            throw new IllegalArgumentException("CPF inválido!");
        }
        this.valor = cleanValor;
    }

    public String getValor() {
        return valor;
    }

    private boolean isValid(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        return validarDigitosVerificadores(cpf);
    }

    private boolean validarDigitosVerificadores(String cpf) {
        int soma = 0;
        int peso = 10;
        for (int i = 0; i < 9; i++) {
            int num = Character.getNumericValue(cpf.charAt(i));
            soma += (num * peso);
            peso--;
        }

        int r = 11 - (soma % 11);
        int digito1 = (r == 10 || r == 11) ? 0 : r;

        soma = 0;
        peso = 11;
        for (int i = 0; i < 10; i++) {
            int num = Character.getNumericValue(cpf.charAt(i));
            soma += (num * peso);
            peso--;
        }

        r = 11 - (soma % 11);
        int digito2 = (r == 10 || r == 11) ? 0 : r;

        int cpfDigito1 = Character.getNumericValue(cpf.charAt(9));
        int cpfDigito2 = Character.getNumericValue(cpf.charAt(10));

        return (digito1 == cpfDigito1 && digito2 == cpfDigito2);
    }

    public String toFormattedString() {
        return valor.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public String toMaskedString() {
        return valor.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.***.***-$4");
    }

    @Override
    public String toString() {
        return toFormattedString();
    }

}
