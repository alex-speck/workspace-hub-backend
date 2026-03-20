package com.projetofullstack.workspace_hub.model.repository;

import com.projetofullstack.workspace_hub.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
