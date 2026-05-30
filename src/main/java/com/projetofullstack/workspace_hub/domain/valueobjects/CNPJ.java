package com.projetofullstack.workspace_hub.domain.valueobjects;

public class CNPJ {

    private String valor;

    public CNPJ(String cnpj){
        if (cnpj == null || isValid(cnpj)) throw new IllegalArgumentException("CNPJ Invalido!");

        this.valor = cnpj;
    }




    private boolean isValid(String cnpj) {
        String cnpjTratado = cnpj.replaceAll("[^0-9]", "");

        if (cnpjTratado.length() != 14 || cnpjTratado.matches("^(\\d)\\1{13}$")) return false;

        return validarDigitosVerificadores(cnpjTratado);
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

    @Override
    public String toString() {
        return valor.replaceAll(
                "(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                "$1.$2.$3/$4-$5"
        );
    }
}
