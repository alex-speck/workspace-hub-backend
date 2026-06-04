package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.TokenAlterarSenhaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenAlterarSenhaUsuarioRepository extends JpaRepository<TokenAlterarSenhaUsuario, Long> {

    Optional<TokenAlterarSenhaUsuario> findByTokenAndDataExpiracaoAfterAndUtilizadoFalse(String token, LocalDateTime hoje);

}
