package com.bellator.bellator_barbearia.auth;

import com.bellator.bellator_barbearia.dto.UsuarioResponse;
import com.bellator.bellator_barbearia.model.Role;

public class AuthResponse {

    private String token;
    private Role role;
    private UsuarioResponse usuario;

    public AuthResponse(String token, Role role, UsuarioResponse usuario) {
        this.token = token;
        this.role = role;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public Role getRole() {
        return role;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }
}