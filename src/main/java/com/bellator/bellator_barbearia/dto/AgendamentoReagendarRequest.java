package com.bellator.bellator_barbearia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendamentoReagendarRequest {
    @NotNull
    public LocalDate data;

    @NotNull
    public LocalTime horario;
}
