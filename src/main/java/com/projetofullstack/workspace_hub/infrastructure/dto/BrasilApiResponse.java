package com.projetofullstack.workspace_hub.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrasilApiResponse {
    private String razao_social;
    private String nome_fantasia;
}
