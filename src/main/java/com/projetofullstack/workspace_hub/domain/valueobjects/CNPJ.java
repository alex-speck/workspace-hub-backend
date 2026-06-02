package com.projetofullstack.workspace_hub.domain.valueobjects;

import com.projetofullstack.workspace_hub.infrastructure.exceptions.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class CNPJ {

    @Column(name = "cnpj")
    private String valor;

    public CNPJ(String valor) {
        if (valor == null) {
            throw new BusinessException("CNPJ não pode ser nulo!");
        }
        String cleanValor = valor.replaceAll("[^0-9]", "");
        if (!isValid(cleanValor)) {
            throw new BusinessException("CNPJ inválido!");
        }
        this.valor = cleanValor;
    }

    public String getValor() {
        return valor;
    }

    private boolean isValid(String cnpj) {
        if (cnpj.length() != 14 || cnpj.matches("^(\\d)\\1{13}$")) return false;

        return validarDigitosVerificadores(cnpj);
    }

    private boolean validarDigitosVerificadores(String cnpj) {
        int[] pesosDigito1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesosDigito2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0;
        for (int i = 0; i < 12; i++) {
            int num = Character.getNumericValue(cnpj.charAt(i));
            soma += num * pesosDigito1[i];
        }

        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;

        soma = 0;
        for (int i = 0; i < 13; i++) {
            int num = Character.getNumericValue(cnpj.charAt(i));
            soma += num * pesosDigito2[i];
        }

        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;

        int cnpjDigito1 = Character.getNumericValue(cnpj.charAt(12));
        int cnpjDigito2 = Character.getNumericValue(cnpj.charAt(13));

        return digito1 == cnpjDigito1 && digito2 == cnpjDigito2;
    }

    public String toFormattedString() {
        return valor.replaceAll(
                "(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                "$1.$2.$3/$4-$5"
        );
    }

    public String toMaskedString() {
        return valor.replaceAll(
                "(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                "$1.***.***/$4-$5"
        );
    }

    @Override
    public String toString() {
        return toFormattedString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CNPJ cnpj = (CNPJ) o;
        return Objects.equals(valor, cnpj.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}
