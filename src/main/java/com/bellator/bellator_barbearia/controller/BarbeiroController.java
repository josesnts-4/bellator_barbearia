package com.bellator.bellator_barbearia.controller;

import com.bellator.bellator_barbearia.dto.UsuarioResponse;
import com.bellator.bellator_barbearia.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    private final UsuarioService usuarioService;

    public BarbeiroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listarBarbeiros().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(),u.getTelefone(), u.getRole()))
                .toList();
    }
}
