package com.projetofullstack.workspace_hub.presentation;

import com.projetofullstack.workspace_hub.application.dto.response.DashboardDataResponse;
import com.projetofullstack.workspace_hub.application.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Controlador para visualização de dados do dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Obter dados do dashboard", description = "Retorna estatísticas, faturamento, ocupação e reservas para o dashboard")
    public ResponseEntity<DashboardDataResponse> getDashboardData() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }
}
