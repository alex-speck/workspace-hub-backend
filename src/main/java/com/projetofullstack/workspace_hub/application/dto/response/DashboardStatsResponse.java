package com.projetofullstack.workspace_hub.application.dto.response;

public record DashboardStatsResponse(
    String title,
    Object value,
    String change,
    String trend,
    String icon
) {}
