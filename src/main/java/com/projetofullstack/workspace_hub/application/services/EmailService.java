package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.application.dto.events.EnviarEmailEvent;
import com.projetofullstack.workspace_hub.domain.entities.Email;
import com.projetofullstack.workspace_hub.domain.enums.EmailTypes;
import com.projetofullstack.workspace_hub.domain.repository.EmailRepository;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailRepository repository;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void enviarEmail(EnviarEmailEvent email) {
        log.info("Iniciando processo de envio de email para: {}", email.to());
        String rawHtml = carregarTemplate(email.template());
        String htmlMessage = substituirVariaveis(rawHtml, email.model());

        var emailPersist = repository.save(
                new Email(email, from, htmlMessage)
        );

        try {
            log.debug("Enviando MimeMessage para: {}", email.to());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setFrom(from);
            helper.setTo(email.to());
            helper.setSubject(email.subject());
            helper.setText(htmlMessage, true);

            mailSender.send(message);

            emailPersist.setEnviado(true);
            emailPersist.setDataEnvio(LocalDateTime.now());
            log.info("Email disparado via JavaMailSender para: {}", email.to());

        } catch (Exception e) {
            log.error("Falha ao enviar email via JavaMailSender para {}: {}", email.to(), e.getMessage());
            emailPersist.setMensagemErro(e.getMessage());
        }

        repository.save(emailPersist);
    }


    public String substituirVariaveis(String html, Map<String, Object> model) {
        if (model == null || model.isEmpty()) {
            html = html.replace("{{ano}}", String.valueOf(LocalDateTime.now().getYear()));
            return html;
        }


        for (var entry : model.entrySet()) {
            if (entry.getValue() != null) {
                html = html.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
            }
        }
        html = html.replace("{{ano}}", String.valueOf(LocalDateTime.now().getYear()));

        return html;
    }

    private String carregarTemplate(EmailTypes template) {
        String htmlMessage = "";
        String path = null;

        switch (template) {
            case CADASTRO -> path = "/templates/cadastro.html";
            case RECUPERACAO_SENHA -> path = "/templates/recuperar.html";
            case RESERVA_CRIADA -> path = "/templates/reserva-criada.html";
            case RESERVA_CANCELADA -> path = "/templates/reserva-cancelada.html";
            case RESERVA_CONCLUIDA -> path = "/templates/reserva-concluida.html";
        }

        try(var inputStream = EmailService.class.getResourceAsStream(path)){
            htmlMessage = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        return htmlMessage;
    }

}
