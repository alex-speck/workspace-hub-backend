package com.projetofullstack.workspace_hub.domain.entities;

import com.projetofullstack.workspace_hub.application.dto.request.ReservaRequest;
import com.projetofullstack.workspace_hub.domain.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "reservas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;

    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;

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

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    public Reserva(ReservaRequest request, Cliente cliente, Espaco espaco, Empresa empresa) {

        if(request.horaFim().isBefore(request.horaInicio())){
            throw new IllegalArgumentException("Hora final deve ser maior que a hora inicial");
        }
        this.codigo = gerarCodigo(empresa.getRazaoSocial());
        this.data = request.data();
        this.horaInicio = request.horaInicio();
        this.horaFim = request.horaFim();
        this.cliente = cliente;
        this.espaco = espaco;
        this.empresa = empresa;
        this.valorHora = espaco.getValorHora();
        this.calcularValorTotal();
    }



    public void calcularValorTotal() {
        double duracaoEmHoras = Duration.between(this.horaInicio, this.horaFim).toMinutes() / 60.0;
        this.valorTotal = duracaoEmHoras * this.valorHora;
    }

    private String gerarCodigo(String nomeEmpresa){
        String prefixo = nomeEmpresa.substring(0, 3).toUpperCase();

        String random = UUID.randomUUID()
                .toString()
                .substring(0, 4)
                .toUpperCase();

        return prefixo + "-" + random;
    }

}
