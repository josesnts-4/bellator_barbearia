package com.bellator.bellator_barbearia.model;

import com.bellator.bellator_barbearia.role.StatusAgendamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "agendamentos",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_barbeiro_data_hora", columnNames = {"barbeiro_id","data","horario"})
        }
)
public class Agendamentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Usuarios cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "barbeiro_id")
    private Usuarios barbeiro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "servico_id")
    private Servicos servicos;

    @NotNull
    private LocalDate data;
    @NotNull
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

}
