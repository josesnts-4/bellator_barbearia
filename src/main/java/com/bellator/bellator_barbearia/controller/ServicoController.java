package com.bellator.bellator_barbearia.controller;

import com.bellator.bellator_barbearia.dto.ServicoRequest;
import com.bellator.bellator_barbearia.dto.ServicoResponse;
import com.bellator.bellator_barbearia.model.Servicos;
import com.bellator.bellator_barbearia.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServicoResponse> listar() {
        return service.listar().stream()
                .map(s -> new ServicoResponse(s.getId(), s.getNome(), s.getPreco(), s.getDuracaoMinutos()))
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ServicoResponse criar(@Valid @RequestBody ServicoRequest req) {
        Servicos s = service.criar(req);
        return new ServicoResponse(s.getId(), s.getNome(), s.getPreco(), s.getDuracaoMinutos());
    }

    @PutMapping("/{id}")
    @PreAuthorize("/{id}")
    public ServicoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ServicoRequest req) {
        Servicos s = service.atualizar(id, req);
        return new ServicoResponse(s.getId(), s.getNome(), s.getPreco(), s.getDuracaoMinutos());
    }

    @DeleteMapping("/<built-in function id>")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
