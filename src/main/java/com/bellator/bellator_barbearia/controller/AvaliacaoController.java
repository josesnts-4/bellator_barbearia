package com.bellator.bellator_barbearia.controller;

import com.bellator.bellator_barbearia.dto.AvaliacaoDTO;
import com.bellator.bellator_barbearia.model.Avaliacao;
import com.bellator.bellator_barbearia.model.Usuarios;
import com.bellator.bellator_barbearia.service.AvaliacaoService;
import com.bellator.bellator_barbearia.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService service;
    private final UsuarioService usuarioService;

    public AvaliacaoController(AvaliacaoService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Avaliacao salvar(Authentication auth, @RequestBody AvaliacaoDTO dto) {
        Usuarios cliente = usuarioService.byEmail(auth.getName());
        
        Avaliacao a = new Avaliacao();
        a.setCliente(cliente);
        a.setNota(dto.nota);
        a.setComentario(dto.comentario);
        
        return service.salvar(a);
    }

    @GetMapping("/media")
    public Double getMedia() {
        return service.buscarMedia();
    }
}
