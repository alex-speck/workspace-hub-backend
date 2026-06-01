package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.application.dto.response.ReservaResumoResponse;
import com.projetofullstack.workspace_hub.infrastructure.exceptions.BusinessException;
import com.projetofullstack.workspace_hub.infrastructure.exceptions.ResourceNotFoundException;
import com.projetofullstack.workspace_hub.application.dto.request.ReservaRequest;
import com.projetofullstack.workspace_hub.application.dto.response.ReservaResponse;
import com.projetofullstack.workspace_hub.application.dto.response.UsuarioLogado;
import com.projetofullstack.workspace_hub.domain.entities.Reserva;
import com.projetofullstack.workspace_hub.domain.enums.StatusCliente;
import com.projetofullstack.workspace_hub.domain.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.domain.enums.StatusReserva;
import com.projetofullstack.workspace_hub.domain.repository.ClienteRepository;
import com.projetofullstack.workspace_hub.domain.repository.EmpresaRepository;
import com.projetofullstack.workspace_hub.domain.repository.EspacoRepository;
import com.projetofullstack.workspace_hub.domain.repository.ReservaRepository;
import com.projetofullstack.workspace_hub.infrastructure.providers.UsuarioLogadoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EspacoRepository espacoRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    public ReservaResponse criarReserva(ReservaRequest request) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();

        if (this.reservaExiste(request.data(), request.horaInicio(), request.horaFim(), request.espacoId(), empresaId)) {
            throw new BusinessException("Já existe uma reserva nesse horário para esse espaço");
        }

        var espaco = espacoRepository.findByIdAndEmpresaId(request.espacoId(), empresaId).orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));
        var cliente = clienteRepository.findByIdAndEmpresaId(request.clienteId(), empresaId).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        var empresa = empresaRepository.getReferenceById(empresaId);

        if (espaco.getStatus() != StatusEspaco.DISPONIVEL || cliente.getStatus() != StatusCliente.ATIVO) {
            throw new BusinessException("Espaço ou cliente não estão disponíveis para reserva");
        }

        Reserva reserva = new Reserva(request, cliente, espaco, empresa);
        reservaRepository.save(reserva);

        return new ReservaResponse(reserva);
    }

    public void cancelarReserva(Long id) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        var reserva = reservaRepository.findByIdAndEmpresaId(id, empresaId).orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada"));
        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    public void concluirReserva(Long id) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        var reserva = reservaRepository.findByIdAndEmpresaId(id, empresaId).orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada"));
        reserva.setStatus(StatusReserva.CONCLUIDA);
        reservaRepository.save(reserva);
    }

    public ReservaResumoResponse listarTodasReservas() {
        UsuarioLogado usuarioLogado = UsuarioLogadoProvider.getUsuarioLogado();
        var hoje = LocalDate.now();

        if (!usuarioLogado.role().equals("ADMIN") && usuarioLogado.empresaId() != null) {
            var reservas = reservaRepository.findAllByEmpresaId(usuarioLogado.empresaId()).stream().map(ReservaResponse::new).toList();

            return new ReservaResumoResponse(
                    reservas.stream().filter(r -> r.data().isEqual(hoje)).toList(),
                    reservas.stream().filter(r -> r.data().isAfter(hoje)).toList()
            );
        }

        var reservas = reservaRepository.findAll().stream().map(ReservaResponse::new).toList();

        return new ReservaResumoResponse(
                reservas.stream().filter(r -> r.data().isEqual(hoje)).toList(),
                reservas.stream().filter(r -> r.data().isAfter(hoje)).toList()
        );
    }

    public ReservaResumoResponse listarReservasPorCliente(Long clienteId) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        var hoje = LocalDate.now();

        if (empresaId != null) {
            var reservas = reservaRepository.findByClienteIdAndEmpresaId(clienteId, empresaId).stream().map(ReservaResponse::new).toList();
            return new ReservaResumoResponse(
                    reservas.stream().filter(r -> r.data().isEqual(hoje)).toList(),
                    reservas.stream().filter(r -> r.data().isAfter(hoje)).toList()
            );
        }
        var reservas = reservaRepository.findByClienteId(clienteId).stream().map(ReservaResponse::new).toList();
        return new ReservaResumoResponse(
                reservas.stream().filter(r -> r.data().isEqual(hoje)).toList(),
                reservas.stream().filter(r -> r.data().isAfter(hoje)).toList()
        );
    }

    private boolean reservaExiste(LocalDate data, LocalTime horaInicio, LocalTime horaFim, Long espacoId, Long empresaId) {
        return reservaRepository.existsByEspacoIdAndStatusAndDataAndHoraInicioBeforeAndHoraFimAfterAndEmpresaId(
                espacoId,
                StatusReserva.ABERTA,
                data,
                horaFim,
                horaInicio,
                empresaId
        );
    }
}
