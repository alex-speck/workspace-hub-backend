package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.Empresa;
import com.projetofullstack.workspace_hub.domain.valueobjects.CNPJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    boolean existsEmpresaByCnpj(CNPJ cnpj);
}
