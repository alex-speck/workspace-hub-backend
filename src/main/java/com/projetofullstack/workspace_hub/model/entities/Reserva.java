package com.projetofullstack.workspace_hub.model.entities;

import com.projetofullstack.workspace_hub.model.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "reservas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    private Double valorHora;
    private Double valorTotal;
    @Enumerated(EnumType.STRING)
    private StatusReserva status = StatusReserva.ABERTA;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "espaco_id")
    private Espaco espaco;

    public void calcularValorTotal() {
        if (this.dataHoraInicio == null || this.dataHoraFim == null) {
            throw new IllegalArgumentException("Datas de início e fim da reserva são obrigatórias para calcular o valor total.");
        }

        long duracaoEmHoras = ChronoUnit.HOURS.between(this.dataHoraInicio, this.dataHoraFim);
        this.valorTotal = duracaoEmHoras * this.valorHora;
    }

}
