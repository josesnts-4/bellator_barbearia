package com.bellator.bellator_barbearia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Usuarios cliente;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer nota;

    private String comentario;

    private LocalDateTime dataCriacao = LocalDateTime.now();
}
