package com.bellator.bellator_barbearia.service;

import com.bellator.bellator_barbearia.exception.ApiException;
import com.bellator.bellator_barbearia.role.Role;
import com.bellator.bellator_barbearia.model.Usuarios;
import com.bellator.bellator_barbearia.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public Usuarios byEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public Usuarios byId(Long id) {
        return repo.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public List<Usuarios> listarBarbeiros() {
        return repo.findByRole(Role.BARBEIRO);
    }
}
