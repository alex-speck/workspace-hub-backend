package com.projetofullstack.workspace_hub.domain.entities;

import com.projetofullstack.workspace_hub.application.dto.request.RegistroEmpresaRequest;
import com.projetofullstack.workspace_hub.domain.valueobjects.CNPJ;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "empresas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razaoSocial;
    private String nomeFantasia;
    @Embedded
    @Column(name = "cnpj")
    private CNPJ cnpj;
    private String email;
    private String telefone;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empresa")
    private List<Usuario> usuarios;

    public Empresa(RegistroEmpresaRequest request) {
        this.razaoSocial = request.razaoSocial();
        this.nomeFantasia = request.nomeFantasia();
        this.cnpj = new CNPJ(request.cnpj());
        this.email = request.email();
        this.telefone = request.telefone();
    }

}
