package com.projetofullstack.workspace_hub.model.entities;

import com.projetofullstack.workspace_hub.model.enums.StatusEspaco;
import com.projetofullstack.workspace_hub.model.enums.TipoEspaco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private TipoEspaco tipo;
    private Double valorHora;
    private StatusEspaco status = StatusEspaco.DISPONIVEL;

}
