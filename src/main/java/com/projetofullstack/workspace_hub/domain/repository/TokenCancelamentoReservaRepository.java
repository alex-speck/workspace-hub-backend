package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.TokenCancelamentoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenCancelamentoReservaRepository extends JpaRepository<TokenCancelamentoReserva, Long> {

    Optional<TokenCancelamentoReserva> findByTokenAndDataExpiracaoAfterAndUtilizadoFalse(String token, LocalDateTime hoje);

}
