package com.bellator.bellator_barbearia.controller;

import com.bellator.bellator_barbearia.dto.AgendamentoCreateRequest;
import com.bellator.bellator_barbearia.dto.AgendamentoResponse;
import com.bellator.bellator_barbearia.model.Agendamentos;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @PostMapping
    public AgendamentoResponse criar(Authentication auth, @RequestBody AgendamentoCreateRequest req) {
        Agendamentos a = service.criar(auth.getName(), req);
        return toResp(a);
    }

    @GetMapping("/me")
    public List<AgendamentoResponse> meus(Authentication auth) {
        return service.meus(auth.getName()).stream().map(this::toResp).toList();
    }

    @PutMapping("/{id}/cancelar")
    public AgendamentoResponse cancelar(Authentication auth, @PathVariable Long id) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return toResp(service.cancelar(id, auth.getName(), isAdmin));
    }
}
