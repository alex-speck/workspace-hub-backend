package com.projetofullstack.workspace_hub.application.dto.events;

import com.projetofullstack.workspace_hub.domain.enums.EmailTypes;

import java.util.Map;

public record EnviarEmailEvent(
        String to,
        String subject,
        EmailTypes template,
        Map<String, Object> model
) {
}
