package com.projetofullstack.workspace_hub.application.dto.response;

import java.util.List;

public record FaturamentoResumoResponse(
    List<FaturamentoDadosResponse> diario,
    List<FaturamentoDadosResponse> semanal,
    List<FaturamentoDadosResponse> mensal
) {}
