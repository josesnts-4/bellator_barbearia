package com.bellator.bellator_barbearia.controller;

import com.bellator.bellator_barbearia.dto.RelatorioResponse;
import com.bellator.bellator_barbearia.model.Agendamento;
import com.bellator.bellator_barbearia.model.StatusAgendamento;
import com.bellator.bellator_barbearia.service.AgendamentoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AgendamentoService agendamentoService;

    public AdminController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping("/agendamentos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Agendamento> listarTodos() {
        return agendamentoService.listarTodos();
    }

    @GetMapping("/relatorio")
    @PreAuthorize("hasRole('ADMIN')")
    public RelatorioResponse relatorio() {
        List<Agendamento> all = agendamentoService.listarTodos();
        long total = all.size();
        long concluidos = all.stream().filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO).count();
        double faturamento = all.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
                .mapToDouble(a -> a.getServico().getPreco() == null ? 0.0 : a.getServico().getPreco())
                .sum();
        return new RelatorioResponse(total, concluidos, faturamento);
    }
}
