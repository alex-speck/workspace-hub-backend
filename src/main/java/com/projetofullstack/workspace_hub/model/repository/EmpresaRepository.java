package com.projetofullstack.workspace_hub.model.repository;

import com.projetofullstack.workspace_hub.model.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    boolean existsEmpresaByCnpj(String cnpj);
}
