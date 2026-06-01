package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.Reserva;
import com.projetofullstack.workspace_hub.domain.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByClienteIdAndEmpresaId(Long clienteId, Long empresaId);

    List<Reserva> findAllByEmpresaId(Long empresaId);

    Optional<Reserva> findByIdAndEmpresaId(Long id, Long empresaId);

    boolean existsByEspacoIdAndStatusAndDataHoraInicioBeforeAndDataHoraFimAfter(Long espacoId, StatusReserva status, LocalDateTime dataHoraFim, LocalDateTime dataHoraInicio);

    boolean existsByEspacoIdAndStatusAndDataHoraInicioBeforeAndDataHoraFimAfterAndEmpresaId(Long espacoId, StatusReserva status, LocalDateTime dataHoraFim, LocalDateTime dataHoraInicio, Long empresaId);
}
