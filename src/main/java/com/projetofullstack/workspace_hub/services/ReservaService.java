package com.projetofullstack.workspace_hub.services;

import com.projetofullstack.workspace_hub.exceptions.BusinessException;
import com.projetofullstack.workspace_hub.exceptions.ResourceNotFoundException;
import com.projetofullstack.workspace_hub.model.dto.request.ReservaRequest;
import com.projetofullstack.workspace_hub.model.dto.response.ReservaResponse;
import com.projetofullstack.workspace_hub.model.entities.Espaco;
import com.projetofullstack.workspace_hub.model.entities.Reserva;
import com.projetofullstack.workspace_hub.model.enums.StatusCliente;
import com.projetofullstack.workspace_hub.model.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.model.enums.StatusReserva;
import com.projetofullstack.workspace_hub.model.repository.ClienteRepository;
import com.projetofullstack.workspace_hub.model.repository.EspacoRepository;
import com.projetofullstack.workspace_hub.model.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EspacoRepository espacoRepository;

    public ReservaResponse criarReserva(ReservaRequest request) {

        if (this.reservaExiste(request.dataHoraInicio(), request.dataHoraFim(), request.espacoId())) {
            throw new BusinessException("Já existe uma reserva nesse horário para esse espaço");
        }

        var espaco = espacoRepository.findById(request.espacoId()).orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));
        var cliente = clienteRepository.findById(request.clienteId()).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        if (espaco.getStatus() != StatusEspaco.DISPONIVEL || cliente.getStatus() != StatusCliente.ATIVO) {
            throw new BusinessException("Espaço ou cliente não estão disponíveis para reserva");
        }

        Reserva reserva = new Reserva();
        reserva.setEspaco(espaco);
        reserva.setCliente(cliente);
        reserva.setDataHoraInicio(request.dataHoraInicio());
        reserva.setDataHoraFim(request.dataHoraFim());
        reserva.setValorHora(espaco.getValorHora());
        reserva.calcularValorTotal();
        reservaRepository.save(reserva);

        return new ReservaResponse(reserva);
    }

    public void cancelarReserva(Long id){
        var reserva = reservaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada"));
        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    public void concluirReserva(Long id){
        var reserva = reservaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada"));
        reserva.setStatus(StatusReserva.CONCLUIDA);
        reservaRepository.save(reserva);
    }

    public List<ReservaResponse> listarTodasReservas() {
        return reservaRepository.findAll().stream().map(ReservaResponse::new).toList();
    }

    public List<ReservaResponse> listarReservasPorCliente(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId).stream().map(ReservaResponse::new).toList();
    }

    private boolean reservaExiste(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, Long espacoId) {
        return reservaRepository.existsByEspacoIdAndStatusAndDataHoraInicioBeforeAndDataHoraFimAfter(espacoId, StatusReserva.ABERTA, dataHoraFim, dataHoraInicio);
    }


}
