package com.projetofullstack.workspace_hub.domain.entities;

import com.projetofullstack.workspace_hub.application.dto.request.EspacoRequest;
import com.projetofullstack.workspace_hub.domain.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.domain.enums.TipoEspaco;
import com.projetofullstack.workspace_hub.domain.valueobjects.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "espacos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Espaco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeNumero;
    @Enumerated(EnumType.STRING)
    private TipoEspaco tipo;
    private Double valorHora;
    @Enumerated(EnumType.STRING)
    private StatusEspaco status = StatusEspaco.DISPONIVEL;

    @Embedded
    private Endereco endereco;

    @OneToMany(mappedBy = "espaco", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    public Espaco(EspacoRequest request, Empresa empresa) {
        this.nomeNumero = request.nomeNumero();
        this.tipo = request.tipo();
        this.valorHora = request.valorHora();
        this.empresa = empresa;
    }

}
