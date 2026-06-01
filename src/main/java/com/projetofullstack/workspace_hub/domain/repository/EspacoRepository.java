package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspacoRepository extends JpaRepository<Espaco, Long> {
    List<Espaco> findAllByEmpresaId(Long empresaId);
    Optional<Espaco> findByIdAndEmpresaId(Long id, Long empresaId);
}
