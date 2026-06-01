package com.projetofullstack.workspace_hub.domain.entities;


import com.projetofullstack.workspace_hub.application.dto.request.CriarUsuarioAdminRequest;
import com.projetofullstack.workspace_hub.application.dto.request.CriarUsuarioRequest;
import com.projetofullstack.workspace_hub.domain.enums.StatusUsuario;
import com.projetofullstack.workspace_hub.domain.valueobjects.CPF;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private StatusUsuario status = StatusUsuario.ATIVO;
    private String role;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    public Usuario(CriarUsuarioRequest request, Empresa empresa) {
        this.nome = request.nome();
        this.email = request.email();
        this.senha = request.senha();
        this.role = "USER";
        this.empresa = empresa;
    }

    public Usuario(CriarUsuarioAdminRequest request) {
        this.nome = request.nome();
        this.email = request.email();
        this.senha = request.senha();
        this.role = "ADMIN";
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public @Nullable String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.nome;
    }
}
