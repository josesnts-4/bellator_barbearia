package com.bellator.bellator_barbearia.dto;

import com.bellator.bellator_barbearia.role.Role;
import lombok.Data;

@Data
public class UsuarioResponse {
    public Long id;
    public String nome;
    public String email;
    public String telefone;
    public Role role;

    public UsuarioResponse(Long id, String nome, String email,String telefone, Role role) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.role = role;
    }
}
