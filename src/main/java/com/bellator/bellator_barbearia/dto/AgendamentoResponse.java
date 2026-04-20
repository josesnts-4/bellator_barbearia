package com.bellator.bellator_barbearia.dto;

import com.bellator.bellator_barbearia.role.StatusAgendamento;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendamentoResponse {
    public Long id;
    public String clienteNome;
    public String barbeiroNome;
    public Long barbeiroId;
    public String servicoNome;
    public Long servicoId;
    public Double servicoPreco;
    public LocalDate data;
    public LocalTime horario;
    public StatusAgendamento status;

    public AgendamentoResponse(Long id, String clienteNome, String barbeiroNome, Long barbeiroId, 
                               String servicoNome, Long servicoId, Double servicoPreco,
                               LocalDate data, LocalTime horario, StatusAgendamento status) {
        this.id = id;
        this.clienteNome = clienteNome;
        this.barbeiroNome = barbeiroNome;
        this.barbeiroId = barbeiroId;
        this.servicoNome = servicoNome;
        this.servicoId = servicoId;
        this.servicoPreco = servicoPreco;
        this.data = data;
        this.horario = horario;
        this.status = status;
    }
}
