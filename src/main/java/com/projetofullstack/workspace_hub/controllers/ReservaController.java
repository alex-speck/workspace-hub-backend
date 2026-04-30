package com.projetofullstack.workspace_hub.controllers;

import com.projetofullstack.workspace_hub.model.dto.request.ReservaRequest;
import com.projetofullstack.workspace_hub.model.dto.response.ReservaResponse;
import com.projetofullstack.workspace_hub.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@Tag(name = "Reserva", description = "Controlador para gerenciamento de reservas de espaços de trabalho")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;


    @GetMapping
    @Operation(summary = "Obter todas as reservas", description = "Retorna uma lista com todas as reservas cadastradas")
    public ResponseEntity<List<ReservaResponse>> listarTodas(){
        return ResponseEntity.ok(reservaService.listarTodasReservas());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova reserva", description = "Cria uma nova reserva de espaço de trabalho")
    public ResponseEntity<?> criarReserva(@RequestBody ReservaRequest request){
        return ResponseEntity.ok(reservaService.criarReserva(request));
    }

    @GetMapping("/cliente/{id}")
    @Operation(summary = "Obter reservas por cliente", description = "Retorna uma lista de reservas associadas a um cliente específico")
    public ResponseEntity<List<ReservaResponse>> listarReservasPorCliente(@PathVariable Long id){
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
    public ResponseEntity<?> excluirReserva(@PathVariable Long id){
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

}
