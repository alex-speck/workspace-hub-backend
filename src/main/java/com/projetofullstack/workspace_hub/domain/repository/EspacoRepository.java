package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspacoRepository extends JpaRepository<Espaco, Long> {
}
