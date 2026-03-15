package com.bellator.bellator_barbearia.repository;

import com.bellator.bellator_barbearia.model.Agendamento;
import com.bellator.bellator_barbearia.model.StatusAgendamento;
import com.bellator.bellator_barbearia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    boolean existsByBarbeiroAndDataAndHorario(Usuario barbeiro, LocalDate data, LocalTime horario);

    List<Agendamento> findByClienteOrderByDataDescHorarioDesc(Usuario cliente);

    List<Agendamento> findByBarbeiroAndDataOrderByHorarioAsc(Usuario barbeiro, LocalDate data);

    List<Agendamento> findByBarbeiroAndDataAndStatusNotOrderByHorarioAsc(Usuario barbeiro, LocalDate data, StatusAgendamento status);
}
