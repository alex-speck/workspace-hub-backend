package com.projetofullstack.workspace_hub.application.dto.response;

import java.util.List;

public record DashboardDataResponse(
    List<DashboardStatsResponse> stats,
    FaturamentoResumoResponse faturamento,
    List<ReservaResponse> reservasHoje,
    List<ReservaResponse> proximasReservas
) {}
