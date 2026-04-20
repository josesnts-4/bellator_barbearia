package com.bellator.bellator_barbearia.controller;

import com.bellator.bellator_barbearia.dto.AgendamentoCreateRequest;
import com.bellator.bellator_barbearia.dto.AgendamentoReagendarRequest;
import com.bellator.bellator_barbearia.dto.AgendamentoResponse;
import com.bellator.bellator_barbearia.model.Agendamentos;
import com.bellator.bellator_barbearia.service.AgendamentoService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public AgendamentoResponse criar(Authentication auth, @RequestBody AgendamentoCreateRequest req) {
        Agendamentos a = service.criar(auth.getName(), req);
        return toResp(a);
    }

    @GetMapping("/me")
    public List<AgendamentoResponse> meus(Authentication auth) {
        return service.meus(auth.getName()).stream().map(this::toResp).toList();
    }

    @GetMapping("/minha-agenda")
    public List<AgendamentoResponse> minhaAgenda(Authentication auth, @RequestParam(required = false) LocalDate data) {
        LocalDate dia = data != null ? data : LocalDate.now();
        return service.agendaBarbeiro(auth.getName(), dia).stream().map(this::toResp).toList();
    }

    @PutMapping("/{id}/concluir")
    public AgendamentoResponse concluir(Authentication auth, @PathVariable Long id) {
        return toResp(service.concluir(id, auth.getName()));
    }

    @PutMapping("/{id}/cancelar")
    public AgendamentoResponse cancelar(Authentication auth, @PathVariable Long id) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return toResp(service.cancelar(id, auth.getName(), isAdmin));
    }

    @PutMapping("/{id}/reagendar")
    public AgendamentoResponse reagendar(Authentication auth, @PathVariable Long id, @RequestBody AgendamentoReagendarRequest req) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return toResp(service.reagendar(id, req.data, req.horario, auth.getName(), isAdmin));
    }

    private AgendamentoResponse toResp(Agendamentos a) {
        return new AgendamentoResponse(
                a.getId(),
                a.getCliente().getNome(),
                a.getBarbeiro().getNome(),
                a.getBarbeiro().getId(),
                a.getServicos().getNome(),
                a.getServicos().getId(),
                a.getServicos().getPreco(),
                a.getData(),
                a.getHorario(),
                a.getStatus());
    }
}
