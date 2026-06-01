package com.projetofullstack.workspace_hub;

import com.projetofullstack.workspace_hub.domain.repository.EmpresaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ReproduceIssueTest {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Test
    void reproduceException() {
        // This is expected to throw an exception if my hypothesis is correct
        empresaRepository.existsEmpresaByCnpj("12345678901234");
    }
}
