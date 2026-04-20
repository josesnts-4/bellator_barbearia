package com.bellator.bellator_barbearia.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvaliacaoDTO {
    @NotNull
    @Min(1)
    @Max(5)
    public Integer nota;
    
    public String comentario;
}
