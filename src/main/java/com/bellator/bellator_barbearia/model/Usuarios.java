package com.bellator.bellator_barbearia.model;

import com.bellator.bellator_barbearia.role.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_email", columnNames = "email")
})
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;
    private String telefone;
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    private Role role;

}
