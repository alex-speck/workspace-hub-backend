package com.projetofullstack.workspace_hub.domain.entities;

import com.projetofullstack.workspace_hub.domain.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserva_id", referencedColumnName = "id")
    private Reserva reserva;

    private StatusReserva statusAnterior;
    private StatusReserva statusNovo;

    private LocalDateTime dataAlteracao;

    private boolean clienteCancelou;

    public HistoricoReserva(Reserva reserva, StatusReserva statusAnterior, StatusReserva statusNovo, boolean clienteCancelou) {
        this.reserva = reserva;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.clienteCancelou = clienteCancelou;
        this.dataAlteracao = LocalDateTime.now();
    }

}
