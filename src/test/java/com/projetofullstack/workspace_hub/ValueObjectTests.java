package com.projetofullstack.workspace_hub;

import com.projetofullstack.workspace_hub.domain.valueobjects.CNPJ;
import com.projetofullstack.workspace_hub.domain.valueobjects.CPF;
import com.projetofullstack.workspace_hub.domain.valueobjects.CPFCNPJ;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValueObjectTests {

    @Test
    void shouldFormatAndMaskCpf() {
        CPF cpf = new CPF("123.456.789-09");
        assertEquals("12345678909", cpf.getValor());
        assertEquals("123.456.789-09", cpf.toFormattedString());
        assertEquals("***.456.789-**", cpf.toMaskedString());
    }

    @Test
    void shouldFormatAndMaskCnpj() {
        CNPJ cnpj = new CNPJ("12.345.678/0001-95");
        assertEquals("12345678000195", cnpj.getValor());
        assertEquals("12.345.678/0001-95", cnpj.toFormattedString());
        assertEquals("**.345.678/0001-95", cnpj.toMaskedString());
    }

    @Test
    void shouldHandleCpfCnpj() {
        CPFCNPJ cpf = new CPFCNPJ("12345678909");
        assertTrue(cpf.isCpf());
        assertEquals("123.456.789-09", cpf.toFormattedString());

        CPFCNPJ cnpj = new CPFCNPJ("12345678000195");
        assertTrue(cnpj.isCnpj());
        assertEquals("12.345.678/0001-95", cnpj.toFormattedString());
    }
}
