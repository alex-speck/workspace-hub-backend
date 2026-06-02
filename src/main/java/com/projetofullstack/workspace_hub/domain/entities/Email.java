package com.projetofullstack.workspace_hub.domain.entities;

import com.projetofullstack.workspace_hub.application.dto.events.EnviarEmailEvent;
import com.projetofullstack.workspace_hub.domain.enums.EmailTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromMail;
    private String toMail;
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    private EmailTypes tipo;

    private boolean enviado;
    private LocalDateTime dataEnvio;
    private String mensagemErro;

    public Email(EnviarEmailEvent email, String from, String body){
        this.fromMail = from;
        this.toMail = email.to();
        this.subject = email.subject();
        this.body = body;
        this.tipo = email.template();

        this.enviado = false;
        this.dataEnvio = null;
    }

    public Email(Email email){
        this.fromMail = email.fromMail;
        this.toMail = email.toMail;
        this.subject = email.subject;
        this.body = email.body;
        this.tipo = email.tipo;
        this.enviado = email.enviado;
        this.dataEnvio = email.dataEnvio;
    }

}
