package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.application.dto.response.*;
import com.projetofullstack.workspace_hub.domain.entities.Email;
import com.projetofullstack.workspace_hub.domain.entities.Espaco;
import com.projetofullstack.workspace_hub.domain.entities.Reserva;
import com.projetofullstack.workspace_hub.domain.enums.StatusReserva;
import com.projetofullstack.workspace_hub.domain.repository.EmailRepository;
import com.projetofullstack.workspace_hub.domain.repository.EspacoRepository;
import com.projetofullstack.workspace_hub.domain.repository.ReservaRepository;
import com.projetofullstack.workspace_hub.infrastructure.providers.UsuarioLogadoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @Autowired
    private EmailRepository emailRepository;

    public DashboardDataResponse getDashboardData() {
        UsuarioLogado usuario = UsuarioLogadoProvider.getUsuarioLogado();
        if (usuario == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        Long empresaId = usuario.empresaId();

        List<Reserva> todasReservas = reservaRepository.findAllByEmpresaId(empresaId);
        List<Espaco> todosEspacos = espacoRepository.findAllByEmpresaId(empresaId); // Should probably filter by company if possible, but Email doesn't have companyId

        LocalDate hoje = LocalDate.now();

        // 1. Stats
        List<DashboardStatsResponse> stats = calculateStats(todasReservas);

        // 2. Faturamento
        FaturamentoResumoResponse faturamento = calculateFaturamento(todasReservas);

        // 3. Reservas Hoje e Próximas
        List<ReservaResponse> reservasHoje = todasReservas.stream()
                .filter(r -> r.getData().isEqual(hoje))
                .map(ReservaResponse::new)
                .collect(Collectors.toList());

        List<ReservaResponse> proximasReservas = todasReservas.stream()
                .filter(r -> r.getData().isAfter(hoje))
                .sorted(Comparator.comparing(Reserva::getData).thenComparing(Reserva::getHoraInicio))
                .limit(5)
                .map(ReservaResponse::new)
                .collect(Collectors.toList());

        // 4. Ocupação Salas
        List<OcupacaoSalaResponse> ocupacaoSalas = calculateOcupacao(todosEspacos, todasReservas, hoje);



        return new DashboardDataResponse(stats, faturamento, reservasHoje, proximasReservas, ocupacaoSalas, null);
    }

    private List<DashboardStatsResponse> calculateStats(List<Reserva> reservas) {
        double receitaTotal = reservas.stream()
                .filter(r -> r.getStatus() != StatusReserva.CANCELADA)
                .mapToDouble(Reserva::getValorTotal)
                .sum();

        long totalReservas = reservas.size();
        long clientesAtivos = reservas.stream().map(r -> r.getCliente().getId()).distinct().count();
        
        // Simple growth placeholder or comparison logic
        return List.of(
            new DashboardStatsResponse("Receita Total", "R$ " + String.format("%.2f", receitaTotal), "+12.5%", "up", "DollarSign"),
            new DashboardStatsResponse("Reservas", totalReservas, "+5.2%", "up", "Calendar"),
            new DashboardStatsResponse("Clientes Ativos", clientesAtivos, "+3.1%", "up", "Users"),
            new DashboardStatsResponse("Taxa de Ocupação", "65%", "-2.4%", "down", "BarChart")
        );
    }

    private FaturamentoResumoResponse calculateFaturamento(List<Reserva> reservas) {
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");

        // Diário (Last 7 days)
        List<FaturamentoDadosResponse> diario = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = hoje.minusDays(i);
            double total = reservas.stream()
                    .filter(r -> r.getData().isEqual(date) && r.getStatus() != StatusReserva.CANCELADA)
                    .mapToDouble(Reserva::getValorTotal).sum();
            diario.add(new FaturamentoDadosResponse(date.format(dayFormatter), total));
        }

        // Mensal (Last 6 months)
        List<FaturamentoDadosResponse> mensal = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate date = hoje.minusMonths(i);
            double total = reservas.stream()
                    .filter(r -> r.getData().getMonth() == date.getMonth() && r.getData().getYear() == date.getYear() && r.getStatus() != StatusReserva.CANCELADA)
                    .mapToDouble(Reserva::getValorTotal).sum();
            mensal.add(new FaturamentoDadosResponse(date.format(monthFormatter), total));
        }

        return new FaturamentoResumoResponse(diario, diario, mensal); // Weekly simplified as daily for now
    }

    private List<OcupacaoSalaResponse> calculateOcupacao(List<Espaco> espacos, List<Reserva> reservas, LocalDate date) {
        return espacos.stream().map(espaco -> {
            double horasOcupadas = reservas.stream()
                    .filter(r -> r.getEspaco().getId().equals(espaco.getId()) && r.getData().isEqual(date) && r.getStatus() != StatusReserva.CANCELADA)
                    .mapToDouble(r -> Duration.between(r.getHoraInicio(), r.getHoraFim()).toMinutes() / 60.0)
                    .sum();
            double percentual = (horasOcupadas / 12.0) * 100.0; // Assuming 12h workday
            return new OcupacaoSalaResponse(espaco.getId(), espaco.getNomeNumero(), Math.min(percentual, 100.0));
        }).collect(Collectors.toList());
    }

}
