package com.projetofullstack.workspace_hub.domain.entities;

import com.projetofullstack.workspace_hub.application.dto.request.RegistroEmpresaDesktopRequest;
import com.projetofullstack.workspace_hub.application.dto.request.RegistroEmpresaRequest;
import com.projetofullstack.workspace_hub.application.dto.response.BuscarCnpjResponse;
import com.projetofullstack.workspace_hub.domain.valueobjects.CNPJ;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private List<Usuario> usuarios = new ArrayList<>();

    public Empresa(RegistroEmpresaRequest request) {
        this.razaoSocial = request.razaoSocial();
        this.nomeFantasia = request.nomeFantasia();
        this.cnpj = new CNPJ(request.cnpj());
        this.email = request.email();
        this.telefone = request.telefone();
    }

    public Empresa(RegistroEmpresaDesktopRequest request, BuscarCnpjResponse dadosEmpresa){
        this.cnpj = new CNPJ(request.cnpj());
        this.razaoSocial = dadosEmpresa.razaoSocial();
        this.nomeFantasia = dadosEmpresa.nomeFantasia();
        this.email = request.email();
        this.telefone = request.telefone();
    }

    public void criarUsuarioPadrao(String email, String senha){
        Usuario retorno = new Usuario();
        retorno.setNome("Administrador - " + this.razaoSocial);
        retorno.setEmail(email);
        retorno.setSenha(senha);
        retorno.setRole("GESTOR");
        retorno.setEmpresa(this);
        this.usuarios.add(retorno);
    }

}
