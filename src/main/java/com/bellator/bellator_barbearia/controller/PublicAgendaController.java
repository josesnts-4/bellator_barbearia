package com.bellator.bellator_barbearia.controller;

import com.bellator.bellator_barbearia.model.Agendamento;
import com.bellator.bellator_barbearia.model.StatusAgendamento;
import com.bellator.bellator_barbearia.model.Usuario;
import com.bellator.bellator_barbearia.repository.AgendamentoRepository;
import com.bellator.bellator_barbearia.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agenda")
public class PublicAgendaController {

    private final UsuarioRepository usuarioRepo;
    private final AgendamentoRepository agendamentoRepo;

    public PublicAgendaController(UsuarioRepository usuarioRepo, AgendamentoRepository agendamentoRepo) {
        this.usuarioRepo = usuarioRepo;
        this.agendamentoRepo = agendamentoRepo;
    }

    /**
     * Endpoint público para o FRONTEND (cliente) checar horários ocupados.
     * Ex: GET /agenda?barbeiroId=2&data=2026-03-03
     */
    @GetMapping
    public List<String> horariosOcupados(@RequestParam Long barbeiroId, @RequestParam LocalDate data) {
        Usuario barbeiro = usuarioRepo.findById(barbeiroId).orElseThrow();
        List<Agendamento> ags = agendamentoRepo
                .findByBarbeiroAndDataAndStatusNotOrderByHorarioAsc(barbeiro, data, StatusAgendamento.CANCELADO);

        return ags.stream().map(a -> a.getHorario().toString()).toList();
    }
}
