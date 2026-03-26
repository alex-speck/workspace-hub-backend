package com.projetofullstack.workspace_hub.model.entities;


import com.projetofullstack.workspace_hub.model.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String senha;

    private StatusUsuario status = StatusUsuario.ATIVO;

}
