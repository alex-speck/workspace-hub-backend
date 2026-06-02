package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.application.dto.events.EnviarEmailEvent;
import com.projetofullstack.workspace_hub.domain.entities.Email;
import com.projetofullstack.workspace_hub.domain.enums.EmailTypes;
import com.projetofullstack.workspace_hub.domain.repository.EmailRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailRepository repository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    private final String from = "test@example.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "from", from);
    }

    @Test
    void enviarEmail_Sucesso() {
        // Arrange
        Map<String, Object> model = new HashMap<>();
        model.put("nome", "User");
        EnviarEmailEvent event = new EnviarEmailEvent("to@example.com", "Subject", EmailTypes.CADASTRO, model);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(repository.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        emailService.enviarEmail(event);

        // Assert
        verify(mailSender).send(mimeMessage);
        verify(repository, times(2)).save(any(Email.class));

        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(repository, times(2)).save(emailCaptor.capture());

        Email savedEmail = emailCaptor.getValue();
        assertTrue(savedEmail.isEnviado());
        assertNull(savedEmail.getMensagemErro());
        assertNotNull(savedEmail.getDataEnvio());
        assertEquals("to@example.com", savedEmail.getToMail());
        assertEquals("Subject", savedEmail.getSubject());
    }

    @Test
    void enviarEmail_ErroNoEnvio() {
        // Arrange
        EnviarEmailEvent event = new EnviarEmailEvent("to@example.com", "Subject", EmailTypes.CADASTRO, new HashMap<>());

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Error sending email")).when(mailSender).send(any(MimeMessage.class));
        when(repository.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        emailService.enviarEmail(event);

        // Assert
        verify(mailSender).send(mimeMessage);
        verify(repository, times(2)).save(any(Email.class));

        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(repository, times(2)).save(emailCaptor.capture());

        Email savedEmail = emailCaptor.getValue();
        assertFalse(savedEmail.isEnviado());
        assertEquals("Error sending email", savedEmail.getMensagemErro());
    }

    @Test
    void enviarEmail_SubstituicaoVariaveis() {
        // Arrange
        Map<String, Object> model = new HashMap<>();
        model.put("nomeEmpresa", "WorkSpace Hub Corp");
        EnviarEmailEvent event = new EnviarEmailEvent("to@example.com", "Subject", EmailTypes.CADASTRO, model);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(repository.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        emailService.enviarEmail(event);

        // Assert
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(repository, atLeastOnce()).save(emailCaptor.capture());

        Email savedEmail = emailCaptor.getAllValues().get(0);
        // "cadastro.html" contains {{nomeEmpresa}}
        assertTrue(savedEmail.getBody().contains("WorkSpace Hub Corp"));
        // Check for "ano" which is added in substituirVariaveis
        assertTrue(savedEmail.getBody().contains(String.valueOf(java.time.LocalDateTime.now().getYear())));
    }
}
