package com.projetofullstack.workspace_hub.application.events;

import com.projetofullstack.workspace_hub.application.dto.events.EnviarEmailEvent;
import com.projetofullstack.workspace_hub.application.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmailEventListener {

    private static final Logger log = LoggerFactory.getLogger(EmailEventListener.class);

    @Autowired
    private EmailService emailService;

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void onEmailEvent(EnviarEmailEvent event){
        log.info("Recebido evento de email para: {}", event.to());
        try {
            emailService.enviarEmail(event);
            log.info("Email enviado com sucesso para: {}", event.to());
        } catch (Exception e) {
            log.error("Erro ao enviar email para {}: {}", event.to(), e.getMessage(), e);
        }
    }

}
