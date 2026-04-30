package com.projetofullstack.workspace_hub.model.repository;

import com.projetofullstack.workspace_hub.model.entities.Reserva;
import com.projetofullstack.workspace_hub.model.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteId(Long clienteId);

    boolean existsByEspacoIdAndStatusAndDataHoraInicioBeforeAndDataHoraFimAfter(Long espacoId, StatusReserva status, LocalDateTime dataHoraFim, LocalDateTime dataHoraInicio);
}
