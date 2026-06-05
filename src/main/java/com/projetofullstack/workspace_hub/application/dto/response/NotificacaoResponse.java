package com.projetofullstack.workspace_hub.application.dto.response;

import java.time.LocalDateTime;

public record NotificacaoResponse(
    Long id,
    String titulo,
    String descricao,
    LocalDateTime data,
    String tipo
) {}
