package com.projetofullstack.workspace_hub;

import com.projetofullstack.workspace_hub.domain.valueobjects.CNPJ;
import com.projetofullstack.workspace_hub.domain.valueobjects.CPF;
import com.projetofullstack.workspace_hub.domain.valueobjects.CPFCNPJ;

public class VerifyVOs {
    public static void main(String[] args) {
        try {
            CPF cpf = new CPF("12345678909");
            System.out.println("CPF Formatted: " + cpf.toFormattedString());
            System.out.println("CPF Masked: " + cpf.toMaskedString());

            CNPJ cnpj = new CNPJ("12345678000195");
            System.out.println("CNPJ Formatted: " + cnpj.toFormattedString());
            System.out.println("CNPJ Masked: " + cnpj.toMaskedString());

            CPFCNPJ cpfcnpjCpf = new CPFCNPJ("123.456.789-09");
            System.out.println("CPFCNPJ (CPF) Formatted: " + cpfcnpjCpf.toFormattedString());
            System.out.println("CPFCNPJ (CPF) Masked: " + cpfcnpjCpf.toMaskedString());

            CPFCNPJ cpfcnpjCnpj = new CPFCNPJ("12.345.678/0001-95");
            System.out.println("CPFCNPJ (CNPJ) Formatted: " + cpfcnpjCnpj.toFormattedString());
            System.out.println("CPFCNPJ (CNPJ) Masked: " + cpfcnpjCnpj.toMaskedString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
