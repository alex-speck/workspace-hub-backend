package com.projetofullstack.workspace_hub.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "token_cancelamento_reserva")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenCancelamentoReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "reserva_id", referencedColumnName = "id")
    private Reserva reserva;

    private boolean utilizado;

    private LocalDateTime dataExpiracao;

    public TokenCancelamentoReserva(Reserva reserva, String token, LocalDateTime dataExpiracao){
        this.reserva = reserva;
        this.token = token;
        this.utilizado = false;
        this.dataExpiracao = dataExpiracao;
    }

}
