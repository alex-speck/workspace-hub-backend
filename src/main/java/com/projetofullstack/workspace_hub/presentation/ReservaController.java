package com.projetofullstack.workspace_hub.presentation;

import com.projetofullstack.workspace_hub.application.dto.request.ReservaRequest;
import com.projetofullstack.workspace_hub.application.dto.response.CancelamentoReservaResumo;
import com.projetofullstack.workspace_hub.application.dto.response.ReservaResumoResponse;
import com.projetofullstack.workspace_hub.application.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
@Tag(name = "Reserva", description = "Controlador para gerenciamento de reservas de espaços de trabalho")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    @Operation(summary = "Obter todas as reservas", description = "Retorna uma lista com todas as reservas cadastradas")
    public ResponseEntity<ReservaResumoResponse> listarTodas(){
        return ResponseEntity.ok(reservaService.listarTodasReservas());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova reserva", description = "Cria uma nova reserva de espaço de trabalho")
    public ResponseEntity<?> criarReserva(@RequestBody @Valid ReservaRequest request){
        return ResponseEntity.ok(reservaService.criarReserva(request));
    }

    @GetMapping("/cliente/{id}")
    @Operation(summary = "Obter reservas por cliente", description = "Retorna uma lista de reservas associadas a um cliente específico")
    public ResponseEntity<ReservaResumoResponse> listarReservasPorCliente(@PathVariable Long id){
        return ResponseEntity.ok(reservaService.listarReservasPorCliente(id));
    }

    @PutMapping("/{id}/concluir")
    @Operation(summary = "Concluir reserva", description = "Marca uma reserva como concluída")
    public ResponseEntity<?> concluirReserva(@PathVariable Long id){
        reservaService.concluirReserva(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar reserva", description = "Cancela a reserva com o id especificado")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id){
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detalhes-cancelamento/{token}")
    @Operation(summary = "Retorna detalhes da reserva", description = "Retorna alguns detalhes da reserva para o cliente confirmar os dados")
    public ResponseEntity<CancelamentoReservaResumo> buscarResumoCancelamento(@PathVariable String token){
        return ResponseEntity.ok(reservaService.buscarDetalhesReservaCancelamento(token));
    }

    @DeleteMapping("/cancelar/{token}")
    @Operation(summary = "Cliente cancelar reserva", description = "Cliente cancela a reserva a partir de um token gerado na criação da reserva")
    public ResponseEntity<?> clienteCancelarReserva(@PathVariable String token){
        reservaService.clienteCancelarReserva(token);
        return ResponseEntity.noContent().build();
    }

}
