package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.application.dto.events.EnviarEmailEvent;
import com.projetofullstack.workspace_hub.application.dto.response.CancelamentoReservaResumo;
import com.projetofullstack.workspace_hub.application.dto.response.ReservaResumoResponse;
import com.projetofullstack.workspace_hub.domain.entities.HistoricoReserva;
import com.projetofullstack.workspace_hub.domain.entities.TokenCancelamentoReserva;
import com.projetofullstack.workspace_hub.domain.enums.EmailTypes;
import com.projetofullstack.workspace_hub.domain.repository.*;
import com.projetofullstack.workspace_hub.infrastructure.exceptions.BusinessException;
import com.projetofullstack.workspace_hub.infrastructure.exceptions.ResourceNotFoundException;
import com.projetofullstack.workspace_hub.application.dto.request.ReservaRequest;
import com.projetofullstack.workspace_hub.application.dto.response.ReservaResponse;
import com.projetofullstack.workspace_hub.application.dto.response.UsuarioLogado;
import com.projetofullstack.workspace_hub.domain.entities.Reserva;
import com.projetofullstack.workspace_hub.domain.enums.StatusCliente;
import com.projetofullstack.workspace_hub.domain.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.domain.enums.StatusReserva;
import com.projetofullstack.workspace_hub.infrastructure.providers.UsuarioLogadoProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private HistoricoReservaRepository historicoReservaRepository;
    @Autowired
    private TokenCancelamentoReservaRepository tokenCancelamentoReservaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EspacoRepository espacoRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
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
        historicoReservaRepository.save(new HistoricoReserva(reserva, null, StatusReserva.ABERTA, false));

        LocalDateTime dataReserva = reserva.getData().atTime(reserva.getHoraInicio());
        LocalDateTime dataExpiracao = dataReserva.minusDays(1);

        var token = tokenCancelamentoReservaRepository.save(new TokenCancelamentoReserva(reserva, gerarCodigoCancelamento(), dataExpiracao));

        applicationEventPublisher.publishEvent(new EnviarEmailEvent(
                cliente.getEmail(),
                "Você tem uma nova Reserva na empresa: " + empresa.getRazaoSocial(),
                EmailTypes.RESERVA_CRIADA,
                Map.of(
                        "nomeUsuario", cliente.getNome(),
                        "nomeEmpresa", empresa.getRazaoSocial(),
                        "nomeEspaco", espaco.getNomeNumero(),
                        "dataReserva", reserva.getData(),
                        "horaInicio", reserva.getHoraInicio(),
                        "horaFim", reserva.getHoraFim(),
                        "localizacao", espaco.getEndereco(),
                        "codigoReserva", reserva.getCodigo(),
                        "linkCancelamento", "http://localhost:3000/cancelar-reserva/" + token.getToken()
                )
        ));

        return new ReservaResponse(reserva);
    }

    @Transactional
    public void cancelarReserva(Long id) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        var reserva = reservaRepository.findByIdAndEmpresaId(id, empresaId).orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada"));

        if (reserva.getStatus() != StatusReserva.ABERTA) {
            return;
        }

        historicoReservaRepository.save(new HistoricoReserva(reserva, reserva.getStatus(), StatusReserva.CANCELADA, false));

        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);


        applicationEventPublisher.publishEvent(new EnviarEmailEvent(
                reserva.getCliente().getEmail(),
                "Sua Reserva #" + reserva.getCodigo() + " foi cancelada",
                EmailTypes.RESERVA_CANCELADA,
                Map.of(
                        "nomeUsuario", reserva.getCliente().getNome(),
                        "nomeEmpresa", reserva.getEmpresa().getRazaoSocial(),
                        "nomeEspaco", reserva.getEspaco().getNomeNumero(),
                        "dataReserva", reserva.getData(),
                        "horaInicio", reserva.getHoraInicio(),
                        "horaFim", reserva.getHoraFim(),
                        "localizacao", reserva.getEspaco().getEndereco(),
                        "codigoReserva", reserva.getCodigo(),
                        "dataCancelamento", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                )
        ));
    }

    @Transactional
    public CancelamentoReservaResumo buscarDetalhesReservaCancelamento(String token) {
        var tokenRecover = tokenCancelamentoReservaRepository
                .findByTokenAndDataExpiracaoAfterAndUtilizadoFalse(token, LocalDateTime.now())
                .orElseThrow(() -> new BusinessException("Token informado é invalido"));

        var reserva = tokenRecover.getReserva();

        return new CancelamentoReservaResumo(
                reserva.getEmpresa().getRazaoSocial(),
                reserva.getCliente().getNome(),
                reserva.getData(),
                reserva.getHoraInicio(),
                reserva.getHoraFim(),
                reserva.getCodigo(),
                reserva.getEspaco().getEndereco().toString(),
                reserva.getEspaco().getNomeNumero()
        );
    }

    @Transactional
    public void concluirReserva(Long id) {
        var empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        var reserva = reservaRepository.findByIdAndEmpresaId(id, empresaId).orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada"));

        if (reserva.getStatus() != StatusReserva.ABERTA) {
            return;
        }

        historicoReservaRepository.save(new HistoricoReserva(reserva, reserva.getStatus(), StatusReserva.CANCELADA, false));

        reserva.setStatus(StatusReserva.CONCLUIDA);
        reservaRepository.save(reserva);

        applicationEventPublisher.publishEvent(new EnviarEmailEvent(
                reserva.getCliente().getEmail(),
                "Sua Reserva #" + reserva.getCodigo() + " foi concluida!",
                EmailTypes.RESERVA_CONCLUIDA,
                Map.of(
                        "nomeUsuario", reserva.getCliente().getNome(),
                        "nomeEmpresa", reserva.getEmpresa().getRazaoSocial(),
                        "nomeEspaco", reserva.getEspaco().getNomeNumero(),
                        "dataReserva", reserva.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        "horaInicio", reserva.getHoraInicio(),
                        "horaFim", reserva.getHoraFim(),
                        "localizacao", reserva.getEspaco().getEndereco(),
                        "codigoReserva", reserva.getCodigo()
                )
        ));
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

    @Transactional
    public void clienteCancelarReserva(String token){
        var tokenRecover = tokenCancelamentoReservaRepository
                .findByTokenAndDataExpiracaoAfterAndUtilizadoFalse(token, LocalDateTime.now())
                .orElseThrow(() -> new BusinessException("Token informado é invalido"));

        tokenRecover.setUtilizado(true);
        var reserva = tokenRecover.getReserva();
        historicoReservaRepository.save(new HistoricoReserva(reserva, reserva.getStatus(), StatusReserva.CANCELADA, true));
        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
        tokenCancelamentoReservaRepository.save(tokenRecover);


        applicationEventPublisher.publishEvent(new EnviarEmailEvent(
                reserva.getCliente().getEmail(),
                "Sua Reserva #" + reserva.getCodigo() + " foi cancelada",
                EmailTypes.RESERVA_CANCELADA,
                Map.of(
                        "nomeUsuario", reserva.getCliente().getNome(),
                        "nomeEmpresa", reserva.getEmpresa().getRazaoSocial(),
                        "nomeEspaco", reserva.getEspaco().getNomeNumero(),
                        "dataReserva", reserva.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        "horaInicio", reserva.getHoraInicio(),
                        "horaFim", reserva.getHoraFim(),
                        "localizacao", reserva.getEspaco().getEndereco(),
                        "codigoReserva", reserva.getCodigo(),
                        "dataCancelamento", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                )
        ));
    }

    private String gerarCodigoCancelamento(){
        return UUID.randomUUID().toString();
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
