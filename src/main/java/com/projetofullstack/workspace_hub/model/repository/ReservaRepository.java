package com.projetofullstack.workspace_hub.model.repository;

import com.projetofullstack.workspace_hub.model.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteId(Long clienteId);

    boolean existsByDataHoraInicioBetweenAndEspacoId(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, Long espacoId);
    boolean existsByDataHoraFimBetweenAndEspacoId(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, Long espacoId);
}
