package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.Reserva;
import com.projetofullstack.workspace_hub.domain.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteId(Long clienteId);

    boolean existsByEspacoIdAndStatusAndDataHoraInicioBeforeAndDataHoraFimAfter(Long espacoId, StatusReserva status, LocalDateTime dataHoraFim, LocalDateTime dataHoraInicio);
}
