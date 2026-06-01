package com.projetofullstack.workspace_hub.domain.entities;


import com.projetofullstack.workspace_hub.application.dto.request.ClienteRequest;
import com.projetofullstack.workspace_hub.domain.enums.StatusCliente;
import com.projetofullstack.workspace_hub.domain.valueobjects.CPFCNPJ;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    @Embedded
    @Column(name = "documento")
    private CPFCNPJ documento;
    private String telefone;
    @Enumerated(EnumType.STRING)
    private StatusCliente status = StatusCliente.ATIVO;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    public Cliente(ClienteRequest request, Empresa empresa) {
        this.nome = request.nome();
        this.documento = new CPFCNPJ(request.documento());
        this.telefone = request.telefone();
        this.empresa = empresa;
    }
}
