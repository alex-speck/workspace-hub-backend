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

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ReservaRepository reservaRepository;

    public DashboardDataResponse getDashboardData() {
        Long empresaId = UsuarioLogadoProvider.getUsuarioLogado().empresaId();
        if (empresaId == null) {
            return null;
        }
        LocalDate hoje = LocalDate.now();

        List<Reserva> todasReservas = reservaRepository.findAllByEmpresaId(empresaId);

        List<ReservaResponse> reservasHoje = todasReservas.stream()
                .filter(r -> r.getData().isEqual(hoje) && r.getStatus() != StatusReserva.CANCELADA)
                .map(ReservaResponse::new)
                .collect(Collectors.toList());

        List<ReservaResponse> proximasReservas = todasReservas.stream()
                .filter(r -> r.getData().isAfter(hoje) && r.getStatus() != StatusReserva.CANCELADA)
                .sorted(Comparator.comparing(Reserva::getData).thenComparing(Reserva::getHoraInicio))
                .limit(5)
                .map(ReservaResponse::new)
                .collect(Collectors.toList());

        return new DashboardDataResponse(
                calculateStats(todasReservas),
                calculateFaturamento(todasReservas),
                reservasHoje,
                proximasReservas
        );
    }

    private List<DashboardStatsResponse> calculateStats(List<Reserva> reservas) {
        double receitaTotal = reservas.stream()
                .filter(r -> r.getStatus() != StatusReserva.CANCELADA)
                .mapToDouble(Reserva::getValorTotal)
                .sum();

        long totalReservas = reservas.stream()
                .filter(r -> r.getStatus() != StatusReserva.CANCELADA)
                .count();

        long clientesAtivos = reservas.stream()
                .filter(r -> r.getStatus() != StatusReserva.CANCELADA)
                .map(r -> r.getCliente().getId())
                .distinct()
                .count();

        return List.of(
                new DashboardStatsResponse("Receita Total", "R$ " + String.format("%.2f", receitaTotal)),
                new DashboardStatsResponse("Reservas", totalReservas),
                new DashboardStatsResponse("Clientes Ativos", clientesAtivos)
        );
    }

    private FaturamentoResumoResponse calculateFaturamento(List<Reserva> reservas) {
        LocalDate hoje = LocalDate.now();
        Locale ptBR = new Locale("pt", "BR");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd/MM", ptBR);
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM", ptBR);

        // Diário: últimos 7 dias
        List<FaturamentoDadosResponse> diario = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = hoje.minusDays(i);
            double total = reservas.stream()
                    .filter(r -> r.getData().isEqual(date) && r.getStatus() != StatusReserva.CANCELADA)
                    .mapToDouble(Reserva::getValorTotal)
                    .sum();
            diario.add(new FaturamentoDadosResponse(date.format(dayFormatter), total));
        }

        // Semanal: últimas 6 semanas
        List<FaturamentoDadosResponse> semanal = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate inicioSemana = hoje.minusWeeks(i).with(DayOfWeek.MONDAY);
            LocalDate fimSemana = inicioSemana.plusDays(6);
            double total = reservas.stream()
                    .filter(r -> {
                        LocalDate data = r.getData();
                        return !data.isBefore(inicioSemana)
                                && !data.isAfter(fimSemana)
                                && r.getStatus() != StatusReserva.CANCELADA;
                    })
                    .mapToDouble(Reserva::getValorTotal)
                    .sum();
            String label = inicioSemana.format(dayFormatter) + " - " + fimSemana.format(dayFormatter);
            semanal.add(new FaturamentoDadosResponse(label, total));
        }

        // Mensal: últimos 6 meses
        List<FaturamentoDadosResponse> mensal = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate date = hoje.minusMonths(i);
            double total = reservas.stream()
                    .filter(r -> r.getData().getMonth() == date.getMonth()
                            && r.getData().getYear() == date.getYear()
                            && r.getStatus() != StatusReserva.CANCELADA)
                    .mapToDouble(Reserva::getValorTotal)
                    .sum();
            mensal.add(new FaturamentoDadosResponse(date.format(monthFormatter), total));
        }

        return new FaturamentoResumoResponse(diario, semanal, mensal);
    }

}
