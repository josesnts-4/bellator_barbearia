package com.bellator.bellator_barbearia.auth;

import lombok.Data;

@Data
public class AuthRequest {

    private String email;
    private String senha;

}