package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAllByEmpresaId(Long empresaId);

    Optional<Usuario> findByIdAndEmpresaId(Long id, Long empresaId);

    boolean existsByEmail(String email);

}
