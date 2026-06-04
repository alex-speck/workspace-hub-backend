package com.projetofullstack.workspace_hub.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "token_alterar_senha_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenAlterarSenhaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    private boolean utilizado;

    private LocalDateTime dataExpiracao = LocalDateTime.now().plusMinutes(15);

    public TokenAlterarSenhaUsuario(Usuario usuario, String token){
        this.usuario = usuario;
        this.token = token;
        this.utilizado = false;
    }

}
