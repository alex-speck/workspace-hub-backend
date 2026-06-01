package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.Reserva;
import com.projetofullstack.workspace_hub.domain.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByClienteIdAndEmpresaId(Long clienteId, Long empresaId);

    List<Reserva> findAllByEmpresaId(Long empresaId);

    Optional<Reserva> findByIdAndEmpresaId(Long id, Long empresaId);

    boolean existsByEspacoIdAndStatusAndDataAndHoraInicioBeforeAndHoraFimAfterAndEmpresaId(Long espacoId, StatusReserva status, LocalDate data, LocalTime horaFim, LocalTime horaInicio, Long empresaId);
}
