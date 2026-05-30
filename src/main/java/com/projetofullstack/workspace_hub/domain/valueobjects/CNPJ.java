package com.projetofullstack.workspace_hub.domain.valueobjects;

public class CNPJ {

    private String valor;

    public CNPJ(String cnpj){
        if (cnpj == null || isValid(cnpj)) throw new IllegalArgumentException("CNPJ Invalido!");

        this.valor = cnpj;
    }




    private boolean isValid(String cnpj) {
        String cnpjTratado = cnpj.replaceAll("[^0-9]", "");

        if (cnpjTratado.length() != 14)
    }

}
